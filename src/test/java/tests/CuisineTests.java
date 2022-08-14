/**
 * Created by Yulya Telysheva
 */
package tests;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.responseSpecification;
import static org.junit.jupiter.api.Assertions.*;

public class CuisineTests extends BasicConfigurations {

    @BeforeEach
    void beforeTest() {
        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectStatusLine("HTTP/1.1 200 OK")
                .expectContentType(ContentType.JSON)
                .expectResponseTime(Matchers.lessThan(2500L))
                .build();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Potato with cheese", "Fish with vegetables"})
    void cuisineValid(String dish) {
        JsonPath response = given()
                .queryParam("apiKey", getApiKey())
                .contentType("application/x-www-form-urlencoded")
                .formParam("title", dish)
                .when()
                .post(getBaseUrl() + "recipes/cuisine")
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .jsonPath();

        ArrayList<String> cuisines = response.get("cuisines");
        assertAll(
                () -> assertEquals(response.get("cuisine"), "Mediterranean"),
                () -> assertEquals("Mediterranean", cuisines.get(0)),
                () -> assertEquals("European", cuisines.get(1)),
                () -> assertEquals("Italian", cuisines.get(2)),
                () -> assertEquals((Float) response.get("confidence"), 0.0f)
        );
    }
}