package com.snwm.api;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.snwm.api.enums.Country;
import com.snwm.api.enums.Service;
import com.snwm.model.GetNumberResponseWrapper;
import com.snwm.model.request.GetCountNumberRequest;
import com.snwm.model.request.GetNumberRequest;
import com.snwm.model.request.GetSmsCodeRequest;
import com.snwm.model.request.ProlongNumberRequest;
import com.snwm.model.request.SetStatusRequest;
import com.snwm.model.response.BalanceResponse;
import com.snwm.model.response.CountryListResponse;
import com.snwm.model.response.GetCountNumberResponse;
import com.snwm.model.response.GetSmsCodeResponse;
import com.snwm.model.response.GetNumberResponse;
import com.snwm.model.response.ProlongNumberResponse;
import com.snwm.model.response.SetStatusResponse;
import com.snwm.util.GetCountNumberResponseDeserializer;
import com.snwm.util.GetSmsCodeResponseDeserializer;

import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Класс VakSmsApi предоставляет методы для взаимодействия с API VakSms.
 * 
 * @author snwm69x
 */

@RequiredArgsConstructor
public class VakSmsApi {
        private static final String BASE_URL = "https://vaksms.ru/api/";
        private final String apiKey;
        private final ApiRequestHandler handler;

        /**
         * Получает баланс пользователя.
         * 
         * @return Объект BalanceResponse, содержащий информацию о балансе пользователя.
         * @throws IOException        если произошла ошибка при отправке HTTP-запроса.
         * @throws VakSmsApiException если API вернуло ошибку.
         */

        private static final String GET_BALANCE = "getBalance/";

        public BalanceResponse getBalance() {
                StringBuilder urlBuilder = new StringBuilder(BASE_URL + GET_BALANCE + "?apiKey=" + apiKey);

                Request request = new Request.Builder()
                                .url(urlBuilder.toString())
                                .build();

                return handler.execute(request, BalanceResponse.class);
        }

        /**
         * Получает количество доступных номеров для указанной услуги, страны и
         * оператора.
         *
         * @param r Объект запроса, содержащий информацию об услуге, стране, операторе и
         *          цене,
         *          для которых требуется получить количество доступных номеров.
         *          Если страна null, используется "ru".
         *          Если оператор null, оператор не указывается.
         *          Если цена true, в ответе будет указана стоимость номера.
         * @return GetCountNumberResponse - Объект ответа, содержащий карту услуг и их
         *         количества, а также потенциальную цену номера.
         *         Ключ карты - это услуга, а значение - это количество доступных
         *         номеров для этой услуги.
         *         Цена представлена в виде числа с плавающей точкой.
         * @throws IOException        Если произошла ошибка при отправке HTTP-запроса.
         * @throws VakSmsApiException Если API вернуло ошибку.
         */

        private static final String GET_COUNT_NUMBER = "getCountNumber/";

        public GetCountNumberResponse getCountNumber(GetCountNumberRequest r) {
                StringBuilder urlBuilder = new StringBuilder(BASE_URL + GET_COUNT_NUMBER + "?apiKey=" + apiKey);
                urlBuilder.append("&service=").append(r.getService().getCode())
                                .append("&country=").append(r.getCountry() == null ? "ru" : r.getCountry().getCode())
                                .append("&operator=").append(r.getOperator() == null ? "" : r.getOperator().getCode());

                if (r.getPrice() != null) {
                        urlBuilder.append("&price=").append(r.getPrice());
                }

                Request request = new Request.Builder()
                                .url(urlBuilder.toString())
                                .build();

                return handler.execute(request, GetCountNumberResponse.class);
        }

        /**
         * Получает список доступных стран.
         *
         * @return Список стран, доступных для использования с API.
         * @throws IOException        если произошла ошибка при отправке HTTP-запроса.
         * @throws VakSmsApiException если API вернуло ошибку.
         */

        private static final String GET_COUNTRY_LIST = "getCountryList/";

        public List<Country> getCountryList() {
                StringBuilder urlBuilder = new StringBuilder(BASE_URL + GET_COUNTRY_LIST + "?apiKey=" + apiKey);

                Request request = new Request.Builder()
                                .url(urlBuilder.toString())
                                .build();

                List<CountryListResponse> countryResponses = handler.execute(request,
                                new TypeToken<List<CountryListResponse>>() {
                                }.getType());

                return countryResponses.stream()
                                .map(cr -> Country.valueOf(cr.getCountryName().toUpperCase().replace(" ", "_")))
                                .collect(Collectors.toList());

        }

        /**
         * Получает номер телефона для указанных услуг.
         *
         * @param r Объект GetNumberRequest, содержащий информацию о том, требуется ли
         *          аренда номера, страну, оператора, идентификатор программного
         *          обеспечения и услуги.
         * @return GetNumberResponseWrapper - Объект ответа, содержащий информацию о
         *         полученном
         *         номере. Если указана одна услуга, возвращается один объект
         *         GetNumberResponse,
         *         если указано две услуги - возвращается массив объектов
         *         GetNumberResponse.
         * @throws IOException              Если произошла ошибка при отправке
         *                                  HTTP-запроса.
         * @throws VakSmsApiException       Если API вернуло ошибку.
         * @throws IllegalArgumentException Если указано более 2 услуг.
         */

        private static final String GET_NUMBER = "getNumber/";

        public GetNumberResponseWrapper getNumber(GetNumberRequest r) {
                if (r.getServices().length > 2) {
                        throw new IllegalArgumentException("You can specify up to 2 services");
                }

                String servicesString = Arrays.stream(r.getServices())
                                .map(Service::getCode)
                                .collect(Collectors.joining(","));

                StringBuilder urlBuilder = new StringBuilder(BASE_URL + GET_NUMBER + "?apiKey=" + apiKey);
                urlBuilder.append("&service=").append(servicesString)
                                .append("&rent=").append(r.getRent() ? "true" : "false")
                                .append("&country=").append(r.getCountry() == null ? "ru" : r.getCountry().getCode())
                                .append("&operator=")
                                .append(r.getOperator() == null ? "None" : r.getOperator().getCode())
                                .append(r.getSoftId() == null ? "" : "&softId=" + r.getSoftId());

                Request request = new Request.Builder()
                                .url(urlBuilder.toString())
                                .build();

                if (r.getServices().length == 1) {
                        return new GetNumberResponseWrapper(r.getServices().length,
                                        new GetNumberResponse[] { handler.execute(request, GetNumberResponse.class) });
                } else {
                        return new GetNumberResponseWrapper(r.getServices().length,
                                        handler.execute(request, GetNumberResponse[].class));
                }
        }

        /**
         * Продлевает время аренды указанного номера для указанной услуги.
         *
         * @param r Объект ProlongNumberRequest, содержащий услугу, для которой
         *          требуется продлить время аренды номера, и номер телефона.
         * @return ProlongNumberResponse - Объект ответа, содержащий информацию о
         *         продлении
         *         номера. Включает в себя номер телефона (tel) и идентификатор номера
         *         (idNum).
         * @throws IOException              Если произошла ошибка при отправке
         *                                  HTTP-запроса.
         * @throws VakSmsApiException       Если API вернуло ошибку.
         * @throws IllegalArgumentException Если услуга или номер телефона не указаны.
         */

        private static final String PROLONG_NUMBER = "prolongNumber/";

        public ProlongNumberResponse prolongNumber(ProlongNumberRequest r) {
                if (r.getService() == null || r.getTel() == null) {
                        throw new IllegalArgumentException("service and tel cannot be null");
                }

                StringBuilder urlBuilder = new StringBuilder(BASE_URL + PROLONG_NUMBER + "?apiKey=" + apiKey);
                urlBuilder.append("&service=").append(r.getService().getCode())
                                .append("&tel=").append(r.getTel());

                Request request = new Request.Builder()
                                .url(urlBuilder.toString())
                                .build();

                return handler.execute(request, ProlongNumberResponse.class);
        }

        /**
         * Устанавливает статус для указанного номера.
         *
         * @param r Объект SetStatusRequest, содержащий статус, который требуется
         *          установить, и идентификатор номера.
         * @return SetStatusResponse - Объект ответа, содержащий информацию об установке
         *         статуса.
         *         Включает в себя статус (status), который был установлен для номера.
         * @throws IOException              Если произошла ошибка при отправке
         *                                  HTTP-запроса.
         * @throws VakSmsApiException       Если API вернуло ошибку.
         * @throws IllegalArgumentException Если статус или идентификатор номера не
         *                                  указаны.
         */

        private static final String SET_STATUS = "setStatus/";

        public SetStatusResponse setStatus(SetStatusRequest r) {
                if (r.getStatus() == null || r.getIdNum() == null) {
                        throw new IllegalArgumentException("status and idNum cannot be null");
                }

                StringBuilder urlBuilder = new StringBuilder(BASE_URL + SET_STATUS + "?apiKey=" + apiKey);
                urlBuilder.append("&status=").append(r.getStatus().getCode())
                                .append("&idNum=").append(r.getIdNum());

                Request request = new Request.Builder()
                                .url(urlBuilder.toString())
                                .build();

                return handler.execute(request, SetStatusResponse.class);
        }

        /**
         * Получает SMS-код для указанного номера.
         *
         * @param r Объект GetSmsCodeRequest, содержащий идентификатор номера и флаг,
         *          указывающий, требуется ли получить все SMS-коды.
         * @return GetSmsCodeResponse - Объект ответа, содержащий информацию о
         *         полученном
         *         SMS-коде. Включает в себя список SMS-кодов (smsCode).
         * @throws IOException        Если произошла ошибка при отправке HTTP-запроса.
         * @throws VakSmsApiException Если API вернуло ошибку.
         */

        private static final String GET_SMS_CODE = "getSmsCode/";

        public GetSmsCodeResponse getSmsCode(GetSmsCodeRequest r) {
                StringBuilder urlBuilder = new StringBuilder(BASE_URL + GET_SMS_CODE + "?apiKey=" + apiKey);
                urlBuilder.append("&idNum=").append(r.getIdNum());

                if (r.getAll() != null && r.getAll()) {
                        urlBuilder.append("&all");
                }

                Request httpRequest = new Request.Builder()
                                .url(urlBuilder.toString())
                                .build();

                return handler.execute(httpRequest, GetSmsCodeResponse.class);
        }

        /**
         * Создает новый экземпляр VakSmsApi с указанным ключом API.
         * Этот метод также настраивает OkHttpClient с заданными временами ожидания
         * чтения и подключения.
         * Кроме того, он создает экземпляр Gson с зарегистрированными десериализаторами
         * для GetSmsCodeResponse и GetCountNumberResponse.
         * И наконец, он создает ApiRequestHandler с созданными OkHttpClient и Gson.
         *
         * @param apiKey Ключ API для использования с API VakSms.
         * @return Новый экземпляр VakSmsApi.
         */

        public static VakSmsApi createWithApiKey(String apiKey) {
                OkHttpClient client = new OkHttpClient.Builder()
                                .readTimeout(30, TimeUnit.SECONDS)
                                .connectTimeout(30, TimeUnit.SECONDS)
                                .build();

                Gson gson = new GsonBuilder()
                                .registerTypeAdapter(GetSmsCodeResponse.class, new GetSmsCodeResponseDeserializer())
                                .registerTypeAdapter(GetCountNumberResponse.class,
                                                new GetCountNumberResponseDeserializer())
                                .create();

                ApiRequestHandler requestHandler = new ApiRequestHandler(client, gson);

                return new VakSmsApi(apiKey, requestHandler);
        }
}
