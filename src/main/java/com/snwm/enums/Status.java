package com.snwm.enums;

// status=send - Еще смс
// status=end - отмена номера
// status=bad - номер уже использован, забанен

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
