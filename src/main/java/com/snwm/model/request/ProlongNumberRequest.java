package com.snwm.model.request;

import com.snwm.api.enums.Service;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProlongNumberRequest {
    private final Service service;
    private final String tel;

    public ProlongNumberRequest(Service service, String tel) {
        if (service == null || tel == null) {
            throw new IllegalArgumentException("service and tel cannot be null");
        }
        this.service = service;
        this.tel = tel;
    }
}
