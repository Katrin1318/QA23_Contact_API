package com.telran.api.tests;

import com.jayway.restassured.RestAssured;
import dto.AuthRequestDto;
import dto.AuthResponseDto;
import dto.ContactDto;
import dto.GetAllContactDto;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class RestAssuredTests {

    public static final String TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6Imtvcm5lcl8xNUBtYWlsLmNvbSJ9.RAGE9ExaVYv1_EOah3nkGHLezauwjCLbInLJn3IVT7k";

    @BeforeMethod
    public void ensurePreconditions() {
        RestAssured.baseURI = "https://contacts-telran.herokuapp.com";
        RestAssured.basePath = "api";
    }

    @Test

    public void loginPositiveTest() {

        AuthRequestDto requestDto = AuthRequestDto.builder()
                .email("korner_15@mail.com")
                .password("Sd3567890$")
                .build();

        AuthResponseDto responseDto = given()
                .contentType("application/json")
                .body(requestDto)
                .post("login")
                .then()
                .assertThat().statusCode(200)
                .extract().response().as(AuthResponseDto.class);

        System.out.println(responseDto.getToken());

        String token2 = given().contentType("application/json")
                .body(requestDto)
                .post("login")
                .then()
                .assertThat().statusCode(200)
                .body(containsString("token"))
                .body("token", equalTo(TOKEN))
                .extract().path("token");

        System.out.println(token2);

    }

    @Test
    public void loginNegativeTest(){
        AuthRequestDto requestDto = AuthRequestDto.builder()
                .email("korner_15@mail.com")
                .password("Sd3567890")
                .build();

        String message = given()
                .contentType("application/json")
                .body(requestDto)
                .post("login")
                .then()
                .assertThat().statusCode(400)
                .extract().path("message");

        System.out.println(message);

    }

    @Test
    public void addContactPositiveTest() {

        ContactDto contactDto = ContactDto.builder()
                .address("Paris")
                .description("feldspieler")
                .email("khr@gm.com")
                .lastName("Han")
                .name("Kora")
                .phone("1234567890").build();

        int id = given().header("Authorization", TOKEN)
                .contentType("application/json")
                .body(contactDto)
                .post("contact")
                .then().assertThat()
                .statusCode(200)
                .extract().path("id");

        System.out.println(id);

    }

    @Test
    public void getAllContactsPositiveTest() {

        GetAllContactDto responceDto = given().header("Authorization", TOKEN)
                .get("contact")
                .then().assertThat()
                .statusCode(200)
                .extract().body().as(GetAllContactDto.class);

        for (ContactDto contact: responceDto.getContacts()) {
            System.out.println(contact.getId() + "***" + contact.getName() + "***");
            System.out.println("***********************************************");
        }

    }

    @Test
    public void deleteContactPositiveTest() {
        String status = given().header("Authorization", TOKEN)
                .delete("contact/33242")
                .then().assertThat()
                .statusCode(200)
                .extract().path("status");
        System.out.println(status);
    }
}
