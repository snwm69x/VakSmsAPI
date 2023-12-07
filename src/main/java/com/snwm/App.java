package com.snwm;

import java.io.IOException;

import com.snwm.api.VakSmsApi;
import com.snwm.api.enums.Country;
import com.snwm.api.enums.Operator;
import com.snwm.api.enums.Service;
import com.snwm.model.ProlongNumberResponse;

public class App {
    public static void main(String[] args) throws IOException {
        VakSmsApi api = VakSmsApi.createWithApiKey("3869942bfca345b4850fbd5963f14496");

        // test 1
        System.out.println("Test 1");
        System.out.println("======");
        System.out.print("Get Balance: ");
        System.out.println(api.getBalance().getBalance());

        // test 2
        System.out.println("Test 2");
        System.out.println("======");
        System.out.println("Get Count Number");
        System.out
                .println(api.getCountNumber(Service.VIBER, Country.RUSSIA, Operator.MTS, true)
                        .get(Service.VIBER.getCode()));
        // test 3
        System.out.println("Test 3");
        System.out.println("======");
        System.out.println("Prolong number");
        ProlongNumberResponse response = api.prolongNumber(Service.TIKTOK, "77756021937");
        System.out.println(response.getIdNum());
        System.out.println(response.getTel());

    }
}
