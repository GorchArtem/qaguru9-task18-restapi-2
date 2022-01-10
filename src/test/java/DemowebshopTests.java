import com.codeborne.selenide.Configuration;
import io.restassured.RestAssured;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;

public class DemowebshopTests {

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "http://demowebshop.tricentis.com/";
        Configuration.baseUrl = "http://demowebshop.tricentis.com/";
    }

    @Test
    @Tags({@Tag("API"), @Tag("UI")})
    void checkCustomersInfo() {
        String login = "artem@qaguru.tr";
        String password = "Artem1349";
//        SelenideElement customersField = $(".fieldset");

        step("Get cookie and set it to browser by API", () -> {
            String authorizationCookie =
                    given()
                    .contentType("application/x-www-form-urlencoded")
                    .formParam("Email", login)
                    .formParam("Password", password)
                    .when()
                    .post("login")
                    .then()
                    .statusCode(302)
                    .extract()
                    .cookie("NOPCOMMERCE.AUTH");

            step("Open minimal content, because cookie can be set when site is opened", () ->
                    open("Themes/DefaultClean/Content/images/logo.png"));

            step("Set cookie to browser", () ->
                    getWebDriver().manage().addCookie(
                            new Cookie("NOPCOMMERCE.AUTH", authorizationCookie)));
        });
        step("Open Customer Info", () ->
                open("http://demowebshop.tricentis.com/customer/info"));

        step("Check firstname", () ->
                $("#FirstName").shouldHave(value("Artem")));
        step("Check lastname", () ->
                $("#LastName").shouldHave(value("Gorchakov")));
        step("Check email", () ->
                $("#Email").shouldHave(value(login)));

    }
}
