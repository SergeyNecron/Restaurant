package ru.study.springboot.to;

import lombok.Getter;
import ru.study.springboot.model.Restaurant;

import javax.validation.constraints.NotNull;

@Getter
public class RestaurantIn {
    @NotNull
    private final String name;
    private String address;

    public RestaurantIn(String name) {
        this.name = name;
    }

    public RestaurantIn(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Restaurant toRestaurant() {
        return new Restaurant(name, address);
    }
}