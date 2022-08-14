/**
 * Created by Yulya Telysheva
 */
package tests;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.kipolad.dto.shoppinglist.add.AddToShoppingList;
import ru.kipolad.dto.shoppinglist.get.Aisle;
import ru.kipolad.dto.shoppinglist.get.GetShoppingList;
import ru.kipolad.dto.shoppinglist.get.Item;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.responseSpecification;
import static org.junit.jupiter.api.Assertions.*;

public class ShoppingListTests extends BasicConfigurations {
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
    void CircleShoppingList() {
        AddToShoppingList addResponse = given()
                .queryParam("apiKey", getApiKey())
                .queryParam("hash", getHash())
                .body("{\n" +
                        "\"item\": \"11 package baking powder\",\n" +
                        "\"aisle\": \"Baking\",\n" +
                        "\"parse\": true\n" +
                        "}")
                .when()
                .post(getBaseUrl() + "mealplanner/kipolad/shopping-list/items")
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(AddToShoppingList.class);

        assertAll(
                () -> assertEquals(addResponse.getName(), "baking powder"),
                () -> assertEquals(addResponse.getAisle(), "Baking"),
                () -> assertEquals(addResponse.getMeasures().getOriginal().getAmount(), 11.0),
                () -> assertEquals(addResponse.getMeasures().getOriginal().getUnit(), "package")
        );

        int id = addResponse.getId();

        GetShoppingList getResponse = given()
                .queryParam("apiKey", getApiKey())
                .queryParam("hash", getHash())
                .when()
                .get(getBaseUrl() + "mealplanner/kipolad/shopping-list")
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(GetShoppingList.class);

        boolean isId = false;
        for (Aisle aisle : getResponse.getAisles()) {
            for (Item item : aisle.getItems()) {
                if (item.getId() == id) {
                    isId = true;
                    break;
                }
            }
            if (isId) break;
        }
        assertTrue(isId, "Get response doesn't include new item.");

        JsonPath response = given()
                .queryParam("apiKey", getApiKey())
                .queryParam("hash", getHash())
                .when()
                .delete(getBaseUrl() + "mealplanner/kipolad/shopping-list/items/" + id)
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .jsonPath();

        assertEquals(response.get("status"), "success", "Item not deleted.");
    }
}
