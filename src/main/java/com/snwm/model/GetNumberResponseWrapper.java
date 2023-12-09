package com.snwm.model;

import com.snwm.model.response.GetNumberResponse;

import lombok.Data;

@Data
public class GetNumberResponseWrapper {
    private final int requestedServices;
    private final GetNumberResponse[] responses;

    public GetNumberResponseWrapper(int requestedServices, GetNumberResponse[] responses) {
        this.requestedServices = requestedServices;
        this.responses = responses;
    }
}
