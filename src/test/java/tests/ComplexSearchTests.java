/**
 * Created by Yulya Telysheva
 */
package tests;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.kipolad.dto.complexsearch.ComplexSearchFat;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class ComplexSearchTests extends BasicConfigurations {
    ResponseSpecification responseSpecification = null;

    @BeforeEach
    void beforeTest() {
        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectStatusLine("HTTP/1.1 200 OK")
                .expectContentType(ContentType.JSON)
                .expectResponseTime(Matchers.lessThan(2000L))
                .build();
    }

    @Test
    void searchRecipes() {
        JsonPath response = given()
                .queryParam("apiKey", getApiKey())
                .when()
                .get(getBaseUrl() + "recipes/complexSearch")
                .then()
                .spec(responseSpecification)
                .extract()
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
                .spec(responseSpecification);
    }


    @ParameterizedTest
    @ValueSource(strings = {"tomato", "cheese", "egg", "carrot"})
    void searchRecipesWithIngredient(String ingredient) {
        JsonPath response = given()
                .queryParam("apiKey", getApiKey())
                .queryParam("query", ingredient)
                .when()
                .get(getBaseUrl() + "recipes/complexSearch")
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .jsonPath();

        int numberFromResponse = response.get("number");
        String titleValue;
        if (numberFromResponse != 0) {
            for (int i = 0; i < numberFromResponse - 1; i++) {
                titleValue = response.get("results.title[" + i + "]").toString().toLowerCase();
                assertTrue(titleValue.contains(ingredient));
            }
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"pork", "beef", "chicken", "fish"})
    void searchRecipesWithDiet(String ingredient) {
        JsonPath response = given()
                .queryParam("apiKey", getApiKey())
                .queryParam("diet", "vegetarian")
                .when()
                .get(getBaseUrl() + "recipes/complexSearch")
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .jsonPath();

        int numberFromResponse = response.get("number");
        String titleValue;
        if (numberFromResponse != 0) {
            for (int i = 0; i < numberFromResponse - 1; i++) {
                titleValue = response.get("results.title[" + i + "]").toString().toLowerCase();
                assertFalse(titleValue.contains(ingredient));
            }
        }
    }

    private static Stream<Arguments> FatAmount() {
        return Stream.of(
                Arguments.of(new ComplexSearchFat(10, 70)),
                Arguments.of(new ComplexSearchFat(20, 60)),
                Arguments.of(new ComplexSearchFat(20, 40)),
                Arguments.of(new ComplexSearchFat(50, 150)));
    }

    @ParameterizedTest
    @MethodSource("FatAmount")
    void complexSearchWithFatAmount(ComplexSearchFat fat) {
        JsonPath response = given()
                .queryParam("apiKey", getApiKey())
                .queryParam("minFat", fat.getMin())
                .queryParam("maxFat", fat.getMax())
                .when()
                .get(getBaseUrl() + "recipes/complexSearch")
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .jsonPath();

        int numberFromResponse = response.get("number");
        if (numberFromResponse != 0) {
            for (int i = 0; i < numberFromResponse - 1; i++) {
                Float amount = response.get("results.nutrition[" + i + "].nutrients.amount[0]");
                assertAll(
                        () -> assertTrue(amount >= fat.getMin()),
                        () -> assertTrue(amount <= fat.getMax())
                );
            }
        }
    }
}
