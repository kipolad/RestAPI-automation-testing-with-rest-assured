/**
 * Created by Yulya Telysheva
 */
package ru.kipolad.dto.shoppinglist.add;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddToShoppingList {
    int id;
    String name;
    Measures measures;
    List<Object> usages = new ArrayList<>();
    List<Object> usageRecipeIds = new ArrayList<Object>();
    boolean pantryItem;
    String aisle;
    Double cost;
    int ingredientId;

    public AddToShoppingList() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Measures getMeasures() {
        return measures;
    }

    public void setMeasures(Measures measures) {
        this.measures = measures;
    }

    public List<Object> getUsages() {
        return usages;
    }

    public void setUsages(List<Object> usages) {
        this.usages = usages;
    }

    public List<Object> getUsageRecipeIds() {
        return usageRecipeIds;
    }

    public void setUsageRecipeIds(List<Object> usageRecipeIds) {
        this.usageRecipeIds = usageRecipeIds;
    }

    public boolean isPantryItem() {
        return pantryItem;
    }

    public void setPantryItem(boolean pantryItem) {
        this.pantryItem = pantryItem;
    }

    public String getAisle() {
        return aisle;
    }

    public void setAisle(String aisle) {
        this.aisle = aisle;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }
}

