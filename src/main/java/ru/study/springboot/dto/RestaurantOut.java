package ru.study.springboot.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.study.springboot.model.Menu;
import ru.study.springboot.model.Restaurant;

import java.time.LocalDate;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor // ! if no create: JsonUtil.java:24 (like default constructor, exist)
public class RestaurantOut extends BaseOut {

    private String address;
    private Integer rating;
    private List<Menu> menus;

    public RestaurantOut(Restaurant restaurant) {
        super(restaurant.getId(), restaurant.getName());
        this.address = restaurant.getAddress();
        this.menus = restaurant.getMenus();
        this.rating = (int) restaurant
                .getVotes()
                .stream()
                .filter(it -> it.getDate().equals(LocalDate.now()))
                .count();

    }

    public RestaurantOut(Integer id, String name, String address) {
        super(id, name);
        this.address = address;
        this.rating = null;
        this.menus = null;
    }
}