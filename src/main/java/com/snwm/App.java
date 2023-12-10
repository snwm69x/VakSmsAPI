package com.snwm;

import java.io.IOException;

import com.snwm.api.VakSmsApi;
import com.snwm.api.async.VakSmsApiAsync;

public class App {
    public static void main(String[] args) throws IOException {
        VakSmsApi api = VakSmsApi.createWithApiKey("your-api-key-here");
        VakSmsApiAsync api_async = VakSmsApiAsync.createWithApiKey("your-api-key-here");
    }
}
