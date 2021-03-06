package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static filters.CustomLogFilter.customLogFilter;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class BookStoreTests {

    @Test
    void simpleGetTest() {
        given()
                .log().uri()
                .get("https://demoqa.com/BookStore/v1/Books")
                .then()
                .log().body()
                .body("books", hasSize(greaterThan(0)));
    }

    @Test
    void authorizeWithAllureTest() {
        String data = "{\"userName\":\"Artem\",\"password\":\"123ASas$$\"}";

        step("Generate Token", () ->
                given()
                        .filter(customLogFilter().withCustomTemplates())
                        .contentType(ContentType.JSON)
                        .body(data)
                        .when()
                        .log().uri()
                        .post("https://demoqa.com/Account/v1/GenerateToken")
                        .then()
                        .log().body()
                        .body("status", is("Success"),
                                "result", is("User authorized successfully."))

        );
    }
}
