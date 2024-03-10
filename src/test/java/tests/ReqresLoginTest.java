package tests;


import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;

public class ReqresLoginTest {

    @Test
    void loginSuccessful() {
        String firstBody = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\"}";
        given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .body(firstBody)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"));

    }

    @Test
    void noContentJSONLoginTest() {
        String firstBody = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\"}";
        given()
                .log().uri()
                .log().method()
                .log().body()
                .body(firstBody)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing email or username"));
    }

    @Test
    void noValueLoginTest() {
        String firstBody = "{}";
        given()
                .log().uri()
                .log().method()
                .log().body()
                .body(firstBody)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing email or username"));
    }

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
                .post("https://reqres.in/api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));


    }

    @Test
    void singleUserSuccessfulLoginWithSchema() {
        given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .when()
                .get("https://reqres.in/api/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/schemaLoginsReqres"));


    }
}