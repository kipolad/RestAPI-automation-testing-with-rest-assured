/**
 * Created by Yulya Telysheva
 */
package tests;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class MealPlannerTest extends BasicConfigurations {
    @Test
    void createAndDeleteItemsTest() {

        int id = given()
                .queryParam("apiKey", getApiKey())
                .queryParam("hash", "29a7f2e9ce17216340b44a33369418d23da42ddd")
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
                .queryParam("hash", "29a7f2e9ce17216340b44a33369418d23da42ddd")
                .delete(getBaseUrl() + "mealplanner/kipolad/items/" + id)
                .then()
                .statusCode(200);
    }
}
