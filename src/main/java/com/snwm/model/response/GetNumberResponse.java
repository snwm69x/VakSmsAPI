package com.snwm.model.response;

import lombok.Data;

@Data
public class GetNumberResponse {
    private Long tel;
    private String idNum;
    private String service; // Это поле будет null, если вы запрашиваете только один сервис
}
