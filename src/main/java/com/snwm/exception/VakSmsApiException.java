package com.snwm.exception;

import com.snwm.enums.ApiError;

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
