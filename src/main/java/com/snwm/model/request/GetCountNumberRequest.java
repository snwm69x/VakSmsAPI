package com.snwm.model.request;

import com.snwm.enums.Country;
import com.snwm.enums.Operator;
import com.snwm.enums.Service;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetCountNumberRequest {
    private final Service service;
    @Builder.Default
    private final Country country = Country.RUSSIA;
    @Builder.Default
    private final Operator operator = Operator.NONE;
    private final Boolean price;
}
