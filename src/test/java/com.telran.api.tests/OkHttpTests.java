package com.telran.api.tests;

import com.google.gson.Gson;
import dto.AuthRequestDto;
import dto.AuthResponseDto;
import dto.ErrorDto;
import okhttp3.*;
import org.testng.annotations.Test;

import java.io.IOException;

public class OkHttpTests {

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    @Test
    public void okHttpLoginTests() throws IOException {

        Gson gson = new Gson();
        OkHttpClient client = new OkHttpClient();

        AuthRequestDto requestDto = AuthRequestDto.builder()
                .email("korner_15@mail.com")
                .password("Sd3567890$")
                .build();

        RequestBody requestBody = RequestBody.create(gson.toJson(requestDto), JSON);

        Request request = new Request.Builder()
                .url("https://contacts-telran.herokuapp.com/api/login")
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();

        String json = response.body().string();

        if (response.isSuccessful()) {
            AuthResponseDto responseDto = gson.fromJson(json, AuthResponseDto.class);
            System.out.println(responseDto.getToken());
        } else {
            ErrorDto errorDto = gson.fromJson(json, ErrorDto.class);
            System.out.println(errorDto.getCode());
            System.out.println(errorDto.getMessage());

        }
    }

}
