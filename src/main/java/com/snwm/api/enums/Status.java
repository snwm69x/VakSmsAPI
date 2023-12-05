package com.snwm.api.enums;

public enum Status {
    SEND("send"),
    END("end"),
    BAD("bad");

    private final String code;

    Status(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
