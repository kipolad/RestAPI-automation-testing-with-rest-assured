/**
 * Created by Yulya Telysheva
 */
package ru.kipolad.dto.shoppinglist.get;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetShoppingList {
    List<Aisle> aisles = new ArrayList<>();
    Double cost;
    int startDate;
    int endDate;

    public GetShoppingList() {
    }

    public List<Aisle> getAisles() {
        return aisles;
    }

    public void setAisles(List<Aisle> aisles) {
        this.aisles = aisles;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public int getStartDate() {
        return startDate;
    }

    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }

    public int getEndDate() {
        return endDate;
    }

    public void setEndDate(int endDate) {
        this.endDate = endDate;
    }
}

