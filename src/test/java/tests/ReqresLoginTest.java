package tests;


import io.qameta.allure.Owner;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ReqresLoginTest {
    @BeforeAll
    static void restAssuredBase() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
    }
    @Owner("iStarzG")
    @Tag("Successful")
    @Test
    void loginSuccessfulWithCorrectValues() {
        String firstBody = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\"}";
        given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .body(firstBody)
                .when()
                .post("/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"));

    }

    @Owner("iStarzG")
    @Tag("Negative")
    @Test
    void noContentJSONLoginTest() {
        String firstBody = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\"}";
        given()
                .log().uri()
                .log().method()
                .log().body()
                .body(firstBody)
                .when()
                .post("/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing email or username"));
    }

    @Owner("iStarzG")
    @Tag("Negative")
    @Test
    void emptyValuesLoginTest() {
        String firstBody = "{}";
        given()
                .log().uri()
                .log().method()
                .log().body()
                .body(firstBody)
                .when()
                .post("/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing email or username"));
    }

    @Owner("iStarzG")
    @Tag("Negative")
    @Test
    void loginUnsuccessfulNoPassword() {
        String firstBody = "{\"email\": \"peter@klaven\"}";
        given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .body(firstBody)
                .when()
                .post("/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));


    }

    @Owner("iStarzG")
    @Tag("Successful")
    @Test
    void singleUserSuccessfulLoginWithSchema() {
        Response statusResponse = given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .when()
                .get("/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/schemaLoginsReqres"))
                .extract().response();
        assertThat(statusResponse.path("data.id"), is(2));
        assertThat(statusResponse.path("data.email"), hasToString("janet.weaver@reqres.in"));
        assertThat(statusResponse.path("data.first_name"), hasToString("Janet"));
        assertThat(statusResponse.path("data.last_name"), hasToString("Weaver"));
    }
}