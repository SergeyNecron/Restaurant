package ru.study.springboot.to;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.Map;

@Data
@Value
@EqualsAndHashCode()
public class MenuTo {
    String nameSaloon;
    Map<String, Double> meals;
    Integer rating;

    public MenuTo(String nameSaloon, Map<String, Double> meals, Integer rating) {
        this.nameSaloon = nameSaloon;
        this.meals = meals;
        this.rating = rating;
    }
}