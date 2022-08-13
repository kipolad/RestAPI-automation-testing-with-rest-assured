/**
 * Created by Yulya Telysheva
 */
package tests;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class CuisineTests extends BasicConfigurations {

    @Test
    void cuisineValid() {
        given()
                .queryParam("apiKey", getApiKey())
                .contentType("application/x-www-form-urlencoded")
                .formParam("title", "Potato with cheese")
                .when()
                .post(getBaseUrl() + "recipes/cuisine")
                .then()
                .statusLine("HTTP/1.1 200 OK")
                .contentType(ContentType.JSON)
                .time(lessThan(2000L));
    }

    @Test
    void cuisineCheckStatus() {
        given()
                .queryParam("apiKey", getApiKey())
                .contentType("application/x-www-form-urlencoded")
                .formParam("title", "Gyoza")
                .when()
                .post(getBaseUrl() + "recipes/cuisine")
                .then()
                .statusLine("HTTP/1.1 200 OK")
                .contentType(ContentType.JSON)
                .time(lessThan(2000L));
    }

    @Test
    void cuisineTest() {
        JsonPath response = given()
                .queryParam("apiKey", getApiKey())
                .contentType("application/x-www-form-urlencoded")
                .formParam("title", "Fried eggplant")
                .when()
                .post(getBaseUrl() + "recipes/cuisine")
                .body()
                .jsonPath();

        assertThat(response.get("cuisine"), equalTo("Mediterranean"));
    }

    @Test
    void cuisinesTest() {
        JsonPath response = given()
                .queryParam("apiKey", getApiKey())
                .contentType("application/x-www-form-urlencoded")
                .formParam("title", "Fried eggplant")
                .when()
                .post(getBaseUrl() + "recipes/cuisine")
                .body()
                .jsonPath();


        ArrayList<String> cuisines = response.get("cuisines");
        assertAll(
                () -> assertEquals("Mediterranean", cuisines.get(0)),
                () -> assertEquals("European", cuisines.get(1)),
                () -> assertEquals("Italian", cuisines.get(2))
        );
    }

    @Test
    void confidenceTest() {
        JsonPath response = given()
                .queryParam("apiKey", getApiKey())
                .contentType("application/x-www-form-urlencoded")
                .formParam("title", "Fried eggplant")
                .when()
                .post(getBaseUrl() + "recipes/cuisine")
                .body()
                .jsonPath();

        assertEquals((Float) response.get("confidence"), 0.0f);
    }

}
