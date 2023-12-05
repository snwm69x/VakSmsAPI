package com.snwm.model;

import java.util.List;

import lombok.Data;

@Data
public class CountryListResponse {
    private String countryName;
    private String countryCode;
    private List<String> operatorList;
}
