package com.snwm.exception;

import com.snwm.api.enums.ApiError;

public class VakSmsApiException extends RuntimeException {
    private ApiError error;

    public VakSmsApiException(ApiError error) {
        super(error.getDescription());
        this.error = error;
    }

    public ApiError getError() {
        return error;
    }
}
