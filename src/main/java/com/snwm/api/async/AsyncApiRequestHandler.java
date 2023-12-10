package com.snwm.api.async;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;

import com.google.gson.Gson;
import com.snwm.enums.ApiError;
import com.snwm.exception.VakSmsApiException;

import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@RequiredArgsConstructor
public class AsyncApiRequestHandler {
    private final OkHttpClient client;
    private final Gson gson;

    public <T> CompletableFuture<T> executeAsync(Request request, Class<T> responseClass) {
        return CompletableFuture.supplyAsync(() -> {
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
        });
    }

    public <T> CompletableFuture<T> executeAsync(Request request, Type typeOfT) {
        return CompletableFuture.supplyAsync(() -> {
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
        });
    }
}
