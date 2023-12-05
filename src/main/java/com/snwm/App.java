package com.snwm;

import java.io.IOException;

import com.snwm.api.VakSmsApi;

public class App {
    public static void main(String[] args) throws IOException {
        VakSmsApi api = VakSmsApi.createWithApiKey("api-key-here");
        System.out.println(api.getBalance().toString());
    }
}
