package com.snwm.util;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.snwm.api.enums.Service;
import com.snwm.model.response.GetCountNumberResponse;

public class GetCountNumberResponseDeserializer implements JsonDeserializer<GetCountNumberResponse> {
    @Override
    public GetCountNumberResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        Map<Service, Integer> serviceCounts = new HashMap<>();
        for (Service service : Service.values()) {
            if (jsonObject.has(service.getCode())) {
                serviceCounts.put(service, jsonObject.get(service.getCode()).getAsInt());
            }
        }

        Double price = jsonObject.has("price") ? jsonObject.get("price").getAsDouble() : null;

        return new GetCountNumberResponse(serviceCounts, price);
    }
}
