package com.snwm.api;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.snwm.api.enums.ApiError;
import com.snwm.api.enums.Country;
import com.snwm.api.enums.Operator;
import com.snwm.api.enums.Service;
import com.snwm.api.enums.Status;
import com.snwm.exception.VakSmsApiException;
import com.snwm.model.ApiResponse;
import com.snwm.model.BalanceResponse;
import com.snwm.model.CountryListResponse;
import com.snwm.model.GetSmsCodeResponse;
import com.snwm.model.NumberResponse;
import com.snwm.model.ProlongNumberResponse;
import com.snwm.model.SetStatusResponse;
import com.snwm.util.GetSmsCodeResponseDeserializer;

import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Класс VakSmsApi предоставляет методы для взаимодействия с API VakSms.
 * Этот класс использует OkHttpClient для отправки HTTP-запросов и Gson для
 * обработки JSON-ответов.
 * 
 * @author snwm69x
 */

@RequiredArgsConstructor
public class VakSmsApi {
    private static final String BASE_URL = "https://vaksms.ru/api/";
    private final OkHttpClient client;
    private final String apiKey;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(GetSmsCodeResponse.class, new GetSmsCodeResponseDeserializer())
            .create();

    /**
     * Получает баланс пользователя.
     * 
     * @return Объект BalanceResponse, содержащий информацию о балансе пользователя.
     * @throws IOException        если произошла ошибка при отправке HTTP-запроса.
     * @throws VakSmsApiException если API вернуло ошибку.
     */

    private static final String GET_BALANCE = "getBalance/";

    public BalanceResponse getBalance() throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + GET_BALANCE + "?apiKey=" + apiKey)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            ApiResponse apiResponse = gson.fromJson(responseBody, ApiResponse.class);
            if (apiResponse.getError() != null) {
                ApiError error = ApiError.fromError(apiResponse.getError());
                throw new VakSmsApiException(error);
            }
            return gson.fromJson(responseBody, BalanceResponse.class);
        }
    }

    /**
     * Получает количество доступных номеров для указанной услуги, страны и
     * оператора.
     *
     * @param service  Услуга, для которой требуется получить количество доступных
     *                 номеров.
     * @param country  Страна, для которой требуется получить количество доступных
     *                 номеров. Если null, используется "ru".
     * @param operator Оператор, для которого требуется получить количество
     *                 доступных номеров. Если null, оператор не указывается.
     * @param price    Если true, в ответе будет указана стоимость номера.
     * @return Map<String, Integer> - String = service, Integer = количество
     *         номеров, содержащий информацию о количестве
     *         доступных номеров.
     * @throws IOException        если произошла ошибка при отправке HTTP-запроса.
     * @throws VakSmsApiException если API вернуло ошибку.
     */

    private static final String GET_COUNT_NUMBER = "getCountNumber/";

    public Map<String, Integer> getCountNumber(Service service, Country country, Operator operator, Boolean price)
            throws IOException {
        String url = BASE_URL + GET_COUNT_NUMBER
                + "?apiKey=" + apiKey
                + "&service=" + service.getCode()
                + "&country=" + (country == null ? "ru" : country.getCode())
                + "&operator=" + (operator == null ? "" : operator.getCode())
                + (price ? "&price=true" : "");

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            ApiResponse apiResponse = gson.fromJson(responseBody, ApiResponse.class);
            if (apiResponse.getError() != null) {
                ApiError error = ApiError.fromError(apiResponse.getError());
                throw new VakSmsApiException(error);
            }
            Type type = new TypeToken<Map<String, Integer>>() {
            }.getType();
            return gson.fromJson(responseBody, type);
        }
    }

    public Map<String, Integer> getCountNumber(Service service, Country country, Operator operator)
            throws IOException {
        return getCountNumber(service, country, operator, false);
    }

    /**
     * Получает список доступных стран.
     *
     * @return Список стран, доступных для использования с API.
     * @throws IOException        если произошла ошибка при отправке HTTP-запроса.
     * @throws VakSmsApiException если API вернуло ошибку.
     */

    private static final String GET_COUNTRY_LIST = "getCountryList/";

    public List<Country> getCountryList() throws IOException {
        String url = BASE_URL + GET_COUNTRY_LIST + "?apiKey=" + apiKey;

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            ApiResponse apiResponse = gson.fromJson(responseBody, ApiResponse.class);
            if (apiResponse.getError() != null) {
                ApiError error = ApiError.fromError(apiResponse.getError());
                throw new VakSmsApiException(error);
            }
            List<CountryListResponse> countryResponses = gson.fromJson(responseBody,
                    new TypeToken<List<CountryListResponse>>() {
                    }.getType());
            return countryResponses.stream()
                    .map(cr -> Country.valueOf(cr.getCountryName().toUpperCase().replace(" ", "_")))
                    .collect(Collectors.toList());
        }
    }

    /**
     * Получает номер телефона для указанных услуг.
     *
     * @param rent     Если true, номер будет арендован.
     * @param country  Страна, для которой требуется получить номер. Если null,
     *                 используется "ru".
     * @param operator Оператор, для которого требуется получить номер. Если null,
     *                 оператор не указывается.
     * @param softId   Идентификатор программного обеспечения, если он есть.
     * @param services Услуги, для которых требуется получить номер. Можно указать
     *                 до 2 услуг.
     * @return Объект NumberResponse, содержащий информацию о полученном номере.
     * @throws IOException              если произошла ошибка при отправке
     *                                  HTTP-запроса.
     * @throws VakSmsApiException       если API вернуло ошибку.
     * @throws IllegalArgumentException если указано более 2 услуг.
     */

    private static final String GET_NUMBER = "getNumber/";

    public NumberResponse getNumber(Boolean rent, Country country, Operator operator, Integer softId, Service service)
            throws IOException {
        return getNumber(rent, country, operator, softId, new Service[] { service })[0];
    }

    public NumberResponse getNumber(Boolean rent, Country country, Operator operator, Service service)
            throws IOException {
        return getNumber(rent, country, operator, null, new Service[] { service })[0];
    }

    public List<NumberResponse> getNumber(Boolean rent, Country country, Operator operator, Integer softId,
            Service service1, Service service2) throws IOException {
        NumberResponse[] responses = getNumber(rent, country, operator, softId, new Service[] { service1, service2 });
        return Arrays.asList(responses);
    }

    public List<NumberResponse> getNumber(Boolean rent, Country country, Operator operator,
            Service service1, Service service2) throws IOException {
        NumberResponse[] responses = getNumber(rent, country, operator, null, new Service[] { service1, service2 });
        return Arrays.asList(responses);
    }

    private NumberResponse[] getNumber(Boolean rent, Country country, Operator operator, Integer softId,
            Service[] services) throws IOException {
        if (services.length > 2) {
            throw new IllegalArgumentException("You can specify up to 2 services");
        }

        String servicesString = Arrays.stream(services)
                .map(Service::getCode)
                .collect(Collectors.joining(","));

        String url = BASE_URL + GET_NUMBER
                + "?apiKey=" + apiKey
                + "&service=" + servicesString
                + "&rent=" + (rent ? "true" : "false")
                + "&country=" + (country == null ? "ru" : country.getCode())
                + "&operator=" + (operator == null ? "None" : operator.getCode())
                + (softId == null ? "" : "&softId=" + softId);

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            ApiResponse apiResponse = gson.fromJson(responseBody, ApiResponse.class);
            if (apiResponse.getError() != null) {
                ApiError error = ApiError.fromError(apiResponse.getError());
                throw new VakSmsApiException(error);
            }
            if (services.length == 1) {
                return new NumberResponse[] { gson.fromJson(responseBody, NumberResponse.class) };
            } else {
                return gson.fromJson(responseBody, NumberResponse[].class);
            }
        }
    }

    /**
     * Продлевает время аренды указанного номера для указанной услуги.
     *
     * @param service Услуга, для которой требуется продлить время аренды номера.
     * @param tel     Номер телефона, время аренды которого требуется продлить.
     * @return Объект ProlongNumberResponse, содержащий информацию о продлении
     *         номера.
     * @throws IOException        если произошла ошибка при отправке HTTP-запроса.
     * @throws VakSmsApiException если API вернуло ошибку.
     */

    private static final String PROLONG_NUMBER = "prolongNumber/";

    public ProlongNumberResponse prolongNumber(Service service, String tel) throws IOException {
        if (service == null || tel == null) {
            throw new IllegalArgumentException("service and tel cannot be null");
        }

        String url = BASE_URL + PROLONG_NUMBER
                + "?apiKey=" + apiKey
                + "&service=" + service.getCode()
                + "&tel=" + tel;

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            ApiResponse apiResponse = gson.fromJson(responseBody, ApiResponse.class);
            if (apiResponse.getError() != null) {
                ApiError error = ApiError.fromError(apiResponse.getError());
                throw new VakSmsApiException(error);
            }
            return gson.fromJson(responseBody, ProlongNumberResponse.class);
        }
    }

    /**
     * Устанавливает статус для указанного номера.
     *
     * @param status Статус, который требуется установить.
     * @param idNum  Идентификатор номера, для которого требуется установить статус.
     * @return Объект SetStatusResponse, содержащий информацию об установке статуса.
     * @throws IOException        если произошла ошибка при отправке HTTP-запроса.
     * @throws VakSmsApiException если API вернуло ошибку.
     */

    private static final String SET_STATUS = "setStatus/";

    public SetStatusResponse setStatus(Status status, String idNum) throws IOException {
        if (status == null || idNum == null) {
            throw new IllegalArgumentException("status and idNum cannot be null");
        }

        String url = BASE_URL + SET_STATUS
                + "?apiKey=" + apiKey
                + "&status=" + status.getCode()
                + "&idNum=" + idNum;

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            ApiResponse apiResponse = gson.fromJson(responseBody, ApiResponse.class);
            if (apiResponse.getError() != null) {
                ApiError error = ApiError.fromError(apiResponse.getError());
                throw new VakSmsApiException(error);
            }
            return gson.fromJson(responseBody, SetStatusResponse.class);
        }
    }

    /**
     * Получает SMS-код для указанного номера.
     *
     * @param idNum Идентификатор номера, для которого требуется получить SMS-код.
     * @param all   Если true, будут получены все SMS-коды для номера.
     * @return Объект GetSmsCodeResponse, содержащий информацию о полученном
     *         SMS-коде.
     * @throws IOException        если произошла ошибка при отправке HTTP-запроса.
     * @throws VakSmsApiException если API вернуло ошибку.
     */

    private static final String GET_SMS_CODE = "getSmsCode/";

    public GetSmsCodeResponse getSmsCode(String idNum, Boolean all) throws IOException {
        if (idNum == null) {
            throw new IllegalArgumentException("idNum cannot be null");
        }

        String url = BASE_URL + GET_SMS_CODE
                + "?apiKey=" + apiKey
                + "&idNum=" + idNum;

        if (all != null && all) {
            url += "&all";
        }

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            ApiResponse apiResponse = gson.fromJson(responseBody, ApiResponse.class);
            if (apiResponse.getError() != null) {
                ApiError error = ApiError.fromError(apiResponse.getError());
                throw new VakSmsApiException(error);
            }
            return gson.fromJson(responseBody, GetSmsCodeResponse.class);
        }
    }

    public GetSmsCodeResponse getSmsCode(String idNum) throws IOException {
        return getSmsCode(idNum, null);
    }

    /**
     * Создает новый экземпляр VakSmsApi с указанным ключом API.
     *
     * @param apiKey Ключ API для использования с API VakSms.
     * @return Новый экземпляр VakSmsApi.
     */

    public static VakSmsApi createWithApiKey(String apiKey) {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();
        return new VakSmsApi(client, apiKey);
    }
}
