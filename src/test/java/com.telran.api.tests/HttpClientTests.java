package com.telran.api.tests;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import dto.AuthRequestDto;
import dto.AuthResponseDto;
import dto.ErrorDto;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HttpClientTests {

    @Test
    public void loginHttpClientTest() throws IOException {
        String email = "korner_15@mail.com";
        String password = "Sd3567890$";

        Response response = Request.Post("https://contacts-telran.herokuapp.com/api/login")
                .bodyString("{\n" +
                        "  \"email\": \"" + email + "\",\n" +
                        "  \"password\": \"" + password + "\"\n" +
                        "}", ContentType.APPLICATION_JSON)
                .execute();

        //System.out.println(response);

        String responseJson = response.returnContent().asString();

        System.out.println(responseJson);

        JsonElement element = JsonParser.parseString(responseJson);
        JsonElement token = element.getAsJsonObject().get("token");

        System.out.println(token.getAsString());

    }

    //eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6Imtvcm5lcl8xNUBtYWlsLmNvbSJ9.RAGE9ExaVYv1_EOah3nkGHLezauwjCLbInLJn3IVT7k

    @Test
    public void loginHttpClientTestWithDto() throws IOException {

        AuthRequestDto requestDto = AuthRequestDto.builder()
                .email("korner_15@mail.com")
                .password("Sd3567890$")
                .build();

        Gson gson = new Gson();

        Response response = Request.Post("https://contacts-telran.herokuapp.com/api/login")
                .bodyString(gson.toJson(requestDto), ContentType.APPLICATION_JSON)
                .execute();

        String json = response.returnContent().asString();

        AuthResponseDto responseDto = gson.fromJson(json, AuthResponseDto.class);
        System.out.println(responseDto.getToken());
    }

    @Test
    public void loginHttpClientNegativeTestWithDto() throws IOException {

        AuthRequestDto requestDto = AuthRequestDto.builder()
                .email("korner_15@mail.com")
                .password("Sd3567890")
                .build();

        Gson gson = new Gson();

        Response response = Request.Post("https://contacts-telran.herokuapp.com/api/login")
                .bodyString(gson.toJson(requestDto), ContentType.APPLICATION_JSON)
                .execute();

        HttpResponse httpResponse = response.returnResponse();
        System.out.println(httpResponse);

        System.out.println(httpResponse.getStatusLine().getStatusCode());

        InputStream inputStream = httpResponse.getEntity().getContent();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        StringBuilder sb = new StringBuilder();

        while ((line = reader.readLine())!=null) {
            sb.append(line);
        }

        ErrorDto error = gson.fromJson(sb.toString(), ErrorDto.class);

        System.out.println(error.getCode());
        System.out.println(error.getMessage());
        System.out.println(error.getDetails());
    }
}


