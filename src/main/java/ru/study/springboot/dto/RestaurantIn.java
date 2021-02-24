package ru.study.springboot.dto;

import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.study.springboot.model.Restaurant;

@Value
@EqualsAndHashCode(callSuper = true)
public class RestaurantIn extends BaseIn {

    String address;

    public RestaurantIn(String name) {
        setName(name);
        address = null;
    }

    public RestaurantIn(String name, String address) {
        setName(name);
        this.address = address;
    }

    public Restaurant toRestaurant() {
        return new Restaurant(null, getName(), address);
    }
}