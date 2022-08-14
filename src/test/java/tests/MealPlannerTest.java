/**
 * Created by Yulya Telysheva
 */
package tests;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.responseSpecification;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MealPlannerTest extends BasicConfigurations {
    @BeforeEach
    void beforeTest() {
        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectStatusLine("HTTP/1.1 200 OK")
                .expectContentType(ContentType.JSON)
                .expectResponseTime(Matchers.lessThan(2500L))
                .build();
    }

    @Test
    void createAndDeleteItemsTest() {

        int id = given()
                .queryParam("apiKey", getApiKey())
                .queryParam("hash", getHash())
                .body("{\n"
                        + " \"date\": 1125864855,\n"
                        + " \"slot\": 1,\n"
                        + " \"position\": 0,\n"
                        + " \"type\": \"INGREDIENTS\",\n"
                        + " \"value\": {\n"
                        + " \"ingredients\": [\n"
                        + " {\n"
                        + " \"name\": \"2 apples\"\n"
                        + " }\n"
                        + " ]\n"
                        + " }\n"
                        + "}")
                .when()
                .post(getBaseUrl() + "mealplanner/kipolad/items")
                .body()
                .jsonPath()
                .get("id");

        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("hash", getHash())
                .delete(getBaseUrl() + "mealplanner/kipolad/items/" + id)
                .then()
                .statusCode(200);
    }
}
