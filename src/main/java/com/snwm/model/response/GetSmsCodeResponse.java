package com.snwm.model.response;

import java.util.List;

import lombok.Data;

@Data
public class GetSmsCodeResponse {
    private List<String> smsCode;
}
