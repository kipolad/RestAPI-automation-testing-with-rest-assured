/**
 * Created by Yulya Telysheva
 */
package ru.kipolad.dto.complexsearch;

public class ComplexSearchFat {
    private final int min;
    private final int max;

    public ComplexSearchFat(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public ComplexSearchFat(int max) {
        this.max = max;
        min = 0;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }
}
