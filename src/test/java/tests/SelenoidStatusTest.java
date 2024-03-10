package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;


public class SelenoidStatusTest {
    @Test
    void checkTotalMini() {
        get("https://selenoid.autotests.cloud/status")
                .then()
                .body("total", is(20));
    }

    @Test
    void checkTotal() {
        given()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .body("total", is(20));
    }

    @Test
    void checkTotalWithLogs() {
        given()
                .log().all()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .log().all()
                .body("total", is(22));
    }

    @Test
    void checkTotalWithSomeLogs() {
        given()
                .log().uri()
                .log().method()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .log().status()
                .log().body()
                .body("total", is(21));
    }

    @Test
    void checkTotalWithStatusCode() {
        given()
                .log().uri()
                .log().method()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("total", is(20));
    }

    @Test
    void checkTotalWithChrome() {
        given()
                .log().uri()
                .log().method()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("total", is(20))
                .body("browsers.chrome", hasKey("100.0"));
    }

    @Test
    void checkTotalWithJsonSchema() {
        given()
                .log().uri()
                .log().method()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/schemaSelenoidStatus.json"))
                .body("total", is(20))
                .body("browsers.chrome", hasKey("100.0"));
    }

    @Test
    void checkTotalWithAssert() {
        Response statusResponse = given()
                .log().uri()
                .log().method()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/schemaSelenoidStatus.json"))
                .extract().response();

//        assertEquals(20, (int) statusResponse.path("total"));
        assertThat(statusResponse.path("total"), is(20));
        assertThat(statusResponse.path("browsers.chrome"), hasKey("100.0"));
    }
}



