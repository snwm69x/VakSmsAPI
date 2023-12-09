package com.snwm.model.response;

import java.util.Map;

import com.snwm.api.enums.Service;

import lombok.Data;

@Data
public class GetCountNumberResponse {
    private final Map<Service, Integer> serviceCounts;
    private final Double price;
}
