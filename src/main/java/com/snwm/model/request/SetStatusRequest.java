package com.snwm.model.request;

import com.snwm.api.enums.Status;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SetStatusRequest {
    private final Status status;
    private final String idNum;

    public SetStatusRequest(Status status, String idNum) {
        if (status == null || idNum == null) {
            throw new IllegalArgumentException("status and idNum cannot be null");
        }
        this.status = status;
        this.idNum = idNum;
    }
}
