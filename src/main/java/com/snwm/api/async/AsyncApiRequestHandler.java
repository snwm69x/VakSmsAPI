package com.snwm.api.async;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;

import com.google.gson.Gson;
import com.snwm.enums.ApiError;
import com.snwm.exception.VakSmsApiException;

import lombok.RequiredArgsConstructor;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@RequiredArgsConstructor
public class AsyncApiRequestHandler {
    private final OkHttpClient client;
    private final Gson gson;

    public <T> CompletableFuture<T> executeAsync(Request request, Class<T> responseClass) {
        final CompletableFuture<T> future = new CompletableFuture<>();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (response) {
                    String responseBody = response.body().string();
                    if (response.isSuccessful()) {
                        T result = gson.fromJson(responseBody, responseClass);
                        future.complete(result);
                    } else {
                        ApiError errorResponse = gson.fromJson(responseBody, ApiError.class);
                        if (errorResponse.getError() != null) {
                            ApiError error = ApiError.fromError(errorResponse.getError());
                            future.completeExceptionally(new VakSmsApiException(error));
                        }
                        future.completeExceptionally(new VakSmsApiException(ApiError.FAILED_REQUEST));
                    }
                }
            }
        });

        return future;
    }

    public <T> CompletableFuture<T> executeAsync(Request request, Type typeOfT) {
        final CompletableFuture<T> future = new CompletableFuture<>();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (response) {
                    String responseBody = response.body().string();
                    if (response.isSuccessful()) {
                        T result = gson.fromJson(responseBody, typeOfT);
                        future.complete(result);
                    } else {
                        ApiError errorResponse = gson.fromJson(responseBody, ApiError.class);
                        if (errorResponse.getError() != null) {
                            ApiError error = ApiError.fromError(errorResponse.getError());
                            future.completeExceptionally(new VakSmsApiException(error));
                        }
                        future.completeExceptionally(new VakSmsApiException(ApiError.FAILED_REQUEST));
                    }
                }
            }
        });

        return future;
    }
}
