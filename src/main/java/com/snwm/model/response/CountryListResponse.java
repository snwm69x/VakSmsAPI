package com.snwm.model.response;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CountryListResponse {
    private String countryName;
    private String countryCode;
    private List<String> operatorList;
}
