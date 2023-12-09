package com.snwm.model.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetSmsCodeRequest {
    private final String idNum;
    private final Boolean all;

    public GetSmsCodeRequest(String idNum, Boolean all) {
        if (idNum == null) {
            throw new IllegalArgumentException("idNum cannot be null");
        }
        this.idNum = idNum;
        this.all = all;
    }
}
