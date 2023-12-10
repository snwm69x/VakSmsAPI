package com.snwm.model.request;

import com.snwm.enums.Country;
import com.snwm.enums.Operator;
import com.snwm.enums.Service;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetNumberRequest {
    @Builder.Default
    private Boolean rent = false;
    @Builder.Default
    private Country country = Country.RUSSIA;
    @Builder.Default
    private Operator operator = Operator.NONE;
    @Builder.Default
    private Integer softId = null;
    private Service[] services;
}
