package com.snwm.model;

import lombok.Data;

@Data
public class NumberResponse {
    private Long tel;
    private String idNum;
    private String service; // Это поле будет null, если вы запрашиваете только один сервис

}
