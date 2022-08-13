/**
 * Created by Yulya Telysheva
 */
package tests;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class ComplexSearchTests extends BasicConfigurations {

    @Test
    void searchRecipes() {
        given()
                .queryParam("apiKey", getApiKey())
                .when()
                .get(getBaseUrl() + "recipes/complexSearch")
                .then()
                .statusLine("HTTP/1.1 200 OK")
                .contentType(ContentType.JSON)
                .time(lessThan(2000L));

        JsonPath response = given()
                .queryParam("apiKey", getApiKey())
                .when()
                .get(getBaseUrl() + "recipes/complexSearch")
                .body()
                .jsonPath();

        assertThat(response.get("offset"), equalTo(0));
        assertThat(response.get("number"), equalTo(10));
    }

    @Test
    void searchRecipesExcludeCuisine() {

        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("excludeCuisine", "British")
                .when()
                .get(getBaseUrl() + "recipes/complexSearch")
                .then()
                .statusLine("HTTP/1.1 200 OK")
                .contentType(ContentType.JSON)
                .time(lessThan(2000L));
    }

    @Test
    void searchRecipesWithTomato() {

        JsonPath response = given()
                .queryParam("apiKey", getApiKey())
                .queryParam("query", "tomato")
                .when()
                .get(getBaseUrl() + "recipes/complexSearch")
                .body()
                .jsonPath();

        int numberFromResponse = response.get("number");
        String titleValue;
        if (numberFromResponse != 0) {
            for (int i = 0; i < numberFromResponse - 1; i++) {
                titleValue = response.get("results.title[" + i + "]");
                titleValue = titleValue.toLowerCase();
                assertTrue(titleValue.contains("tomato"));
            }
        }
    }

    @Test
    void searchRecipesIncludeIngredients() {

        JsonPath response = given()
                .queryParam("apiKey", getApiKey())
                .queryParam("diet", "vegetarian")
                .when()
                .get(getBaseUrl() + "recipes/complexSearch")
                .body()
                .jsonPath();

        int numberFromResponse = response.get("number");
        String titleValue;
        if (numberFromResponse != 0) {
            for (int i = 0; i < numberFromResponse - 1; i++) {
                titleValue = response.get("results.title[" + i + "]");
                String finalTitleValue = titleValue.toLowerCase();
                assertAll(
                        () -> assertFalse(finalTitleValue.contains("pork")),
                        () -> assertFalse(finalTitleValue.contains("beef")),
                        () -> assertFalse(finalTitleValue.contains("chicken"))
                );
            }
        }
    }

    @Test
    void searchRecipesMinMaxFat() {

        JsonPath response = given()
                .queryParam("apiKey", getApiKey())
                .queryParam("minFat", "20")
                .queryParam("maxFat", "70")
                .when()
                .get(getBaseUrl() + "recipes/complexSearch")
                .body()
                .jsonPath();

        int numberFromResponse = response.get("number");
        Float amount;
        if (numberFromResponse != 0) {
            for (int i = 0; i < numberFromResponse - 1; i++) {
                amount = response.get("results.nutrition[" + i + "].nutrients.amount[0]");
                Float finalAmount = amount;
                assertAll(
                        () -> assertTrue(finalAmount > 20),
                        () -> assertTrue(finalAmount < 70)
                );
            }
        }
    }
}
