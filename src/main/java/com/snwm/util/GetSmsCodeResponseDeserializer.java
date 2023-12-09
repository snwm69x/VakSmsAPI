package com.snwm.util;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.snwm.model.response.GetSmsCodeResponse;

public class GetSmsCodeResponseDeserializer implements JsonDeserializer<GetSmsCodeResponse> {
    @Override
    public GetSmsCodeResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        GetSmsCodeResponse response = new GetSmsCodeResponse();
        JsonElement smsCodeElement = json.getAsJsonObject().get("smsCode");
        if (smsCodeElement.isJsonArray()) {
            // If smsCode is an array, deserialize it as a list of strings
            response.setSmsCode(context.deserialize(smsCodeElement, new TypeToken<List<String>>() {
            }.getType()));
        } else {
            // If smsCode is a single string, add it to a new list
            response.setSmsCode(Collections.singletonList(smsCodeElement.getAsString()));
        }
        return response;
    }
}
