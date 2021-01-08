package ru.study.springboot.to;

import lombok.*;

import java.util.Map;
@Data
@Value
@EqualsAndHashCode()
public class MenuRating {
    String nameSaloon;
    Map<String, Double> meals;
    Integer rating;

    public MenuRating(String nameSaloon, Map<String, Double> meals, Integer rating) {
        this.nameSaloon = nameSaloon;
        this.meals = meals;
        this.rating = rating;
    }
}