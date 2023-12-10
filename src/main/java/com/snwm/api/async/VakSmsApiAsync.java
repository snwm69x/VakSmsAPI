package com.snwm.api.async;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.snwm.constant.VakSmsApiConstants;
import com.snwm.enums.Country;
import com.snwm.enums.Service;
import com.snwm.model.GetNumberResponseWrapper;
import com.snwm.model.request.GetCountNumberRequest;
import com.snwm.model.request.GetNumberRequest;
import com.snwm.model.request.GetSmsCodeRequest;
import com.snwm.model.request.ProlongNumberRequest;
import com.snwm.model.request.SetStatusRequest;
import com.snwm.model.response.BalanceResponse;
import com.snwm.model.response.CountryListResponse;
import com.snwm.model.response.GetCountNumberResponse;
import com.snwm.model.response.GetNumberResponse;
import com.snwm.model.response.GetSmsCodeResponse;
import com.snwm.model.response.ProlongNumberResponse;
import com.snwm.model.response.SetStatusResponse;
import com.snwm.util.GetCountNumberResponseDeserializer;
import com.snwm.util.GetSmsCodeResponseDeserializer;

import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;

@RequiredArgsConstructor
public class VakSmsApiAsync {
    private final String apiKey;
    private final AsyncApiRequestHandler handler;

    public CompletableFuture<BalanceResponse> getBalance() {
        StringBuilder urlBuilder = new StringBuilder(
                VakSmsApiConstants.BASE_API_URL + VakSmsApiConstants.GET_BALANCE + "?apiKey=" + apiKey);

        Request request = new Request.Builder()
                .url(urlBuilder.toString())
                .build();

        return handler.executeAsync(request, BalanceResponse.class);
    }

    public CompletableFuture<GetCountNumberResponse> getCountNumber(GetCountNumberRequest r) {
        StringBuilder urlBuilder = new StringBuilder(VakSmsApiConstants.BASE_API_URL
                + VakSmsApiConstants.GET_COUNT_NUMBER + "?apiKey=" + apiKey);
        urlBuilder.append("&service=").append(r.getService().getCode())
                .append("&country=").append(r.getCountry() == null ? "ru" : r.getCountry().getCode())
                .append("&operator=").append(r.getOperator() == null ? "" : r.getOperator().getCode());

        if (r.getPrice() != null) {
            urlBuilder.append("&price=").append(r.getPrice());
        }

        Request request = new Request.Builder()
                .url(urlBuilder.toString())
                .build();

        return handler.executeAsync(request, GetCountNumberResponse.class);
    }

    public CompletableFuture<List<Country>> getCountryList() {
        StringBuilder urlBuilder = new StringBuilder(VakSmsApiConstants.BASE_API_URL
                + VakSmsApiConstants.GET_COUNTRY_LIST + "?apiKey=" + apiKey);

        Request request = new Request.Builder()
                .url(urlBuilder.toString())
                .build();

        return handler.executeAsync(request, new TypeToken<List<CountryListResponse>>() {
        }.getType())
                .thenApply(countryResponses -> ((List<CountryListResponse>) countryResponses).stream()
                        .map(cr -> Country.valueOf(cr.getCountryName().toUpperCase().replace(" ", "_")))
                        .collect(Collectors.toList()));
    }

    public CompletableFuture<GetNumberResponseWrapper> getNumber(GetNumberRequest r) {
        if (r.getServices().length > 2) {
            throw new IllegalArgumentException("You can specify up to 2 services");
        }

        String servicesString = Arrays.stream(r.getServices())
                .map(Service::getCode)
                .collect(Collectors.joining(","));

        StringBuilder urlBuilder = new StringBuilder(
                VakSmsApiConstants.BASE_API_URL + VakSmsApiConstants.GET_NUMBER + "?apiKey=" + apiKey);
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
            return handler.executeAsync(request, GetNumberResponse.class)
                    .thenApply(response -> new GetNumberResponseWrapper(r.getServices().length,
                            new GetNumberResponse[] { response }));
        } else {
            return handler.executeAsync(request, GetNumberResponse[].class)
                    .thenApply(response -> new GetNumberResponseWrapper(r.getServices().length, response));
        }
    }

    public CompletableFuture<ProlongNumberResponse> prolongNumber(ProlongNumberRequest r) {
        if (r.getService() == null || r.getTel() == null) {
            throw new IllegalArgumentException("service and tel cannot be null");
        }

        StringBuilder urlBuilder = new StringBuilder(VakSmsApiConstants.BASE_API_URL
                + VakSmsApiConstants.PROLONG_NUMBER + "?apiKey=" + apiKey);
        urlBuilder.append("&service=").append(r.getService().getCode())
                .append("&tel=").append(r.getTel());

        Request request = new Request.Builder()
                .url(urlBuilder.toString())
                .build();

        return handler.executeAsync(request, ProlongNumberResponse.class);
    }

    public CompletableFuture<SetStatusResponse> setStatus(SetStatusRequest r) {
        if (r.getStatus() == null || r.getIdNum() == null) {
            throw new IllegalArgumentException("status and idNum cannot be null");
        }

        StringBuilder urlBuilder = new StringBuilder(
                VakSmsApiConstants.BASE_API_URL + VakSmsApiConstants.SET_STATUS + "?apiKey=" + apiKey);
        urlBuilder.append("&status=").append(r.getStatus().getCode())
                .append("&idNum=").append(r.getIdNum());

        Request request = new Request.Builder()
                .url(urlBuilder.toString())
                .build();

        return handler.executeAsync(request, SetStatusResponse.class);
    }

    public CompletableFuture<GetSmsCodeResponse> getSmsCode(GetSmsCodeRequest r) {
        StringBuilder urlBuilder = new StringBuilder(VakSmsApiConstants.BASE_API_URL
                + VakSmsApiConstants.GET_SMS_CODE + "?apiKey=" + apiKey);
        urlBuilder.append("&idNum=").append(r.getIdNum());

        if (r.getAll() != null && r.getAll()) {
            urlBuilder.append("&all");
        }

        Request httpRequest = new Request.Builder()
                .url(urlBuilder.toString())
                .build();

        return handler.executeAsync(httpRequest, GetSmsCodeResponse.class);
    }

    public static VakSmsApiAsync createWithApiKey(String apiKey) {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(GetSmsCodeResponse.class, new GetSmsCodeResponseDeserializer())
                .registerTypeAdapter(GetCountNumberResponse.class,
                        new GetCountNumberResponseDeserializer())
                .create();

        AsyncApiRequestHandler requestHandler = new AsyncApiRequestHandler(client, gson);

        return new VakSmsApiAsync(apiKey, requestHandler);
    }
}
