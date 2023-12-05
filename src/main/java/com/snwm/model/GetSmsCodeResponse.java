package com.snwm.model;

import java.util.List;

import lombok.Data;

@Data
public class GetSmsCodeResponse {
    private List<String> smsCode;
}
