package com.snwm.api;

import java.io.IOException;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.snwm.enums.ApiError;
import com.snwm.exception.VakSmsApiException;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@RequiredArgsConstructor
public class ApiRequestHandler {
    private final OkHttpClient client;
    private final Gson gson;

    public <T> T execute(Request request, Class<T> responseClass) {
        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            if (response.isSuccessful()) {
                return gson.fromJson(responseBody, responseClass);
            } else {
                ApiError errorResponse = gson.fromJson(responseBody, ApiError.class);
                if (errorResponse.getError() != null) {
                    ApiError error = ApiError.fromError(errorResponse.getError());
                    throw new VakSmsApiException(error);
                }
                throw new VakSmsApiException(ApiError.FAILED_REQUEST);
            }
        } catch (IOException e) {
            throw new VakSmsApiException(ApiError.FAILED_REQUEST);
        }
    }

    public <T> T execute(Request request, Type typeOfT) {
        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            if (response.isSuccessful()) {
                return gson.fromJson(responseBody, typeOfT);
            } else {
                ApiError errorResponse = gson.fromJson(responseBody, ApiError.class);
                if (errorResponse.getError() != null) {
                    ApiError error = ApiError.fromError(errorResponse.getError());
                    throw new VakSmsApiException(error);
                }
                throw new VakSmsApiException(ApiError.FAILED_REQUEST);
            }
        } catch (IOException e) {
            throw new VakSmsApiException(ApiError.FAILED_REQUEST);
        }
    }
}
