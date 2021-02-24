package ru.study.springboot.to;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.study.springboot.model.Menu;
import ru.study.springboot.model.Restaurant;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor // ! if no create: JsonUtil.java:24 (like default constructor, exist)
public class RestaurantOut extends BaseOut {

    private String address;
    private Integer rating;
    private List<Menu> menus;

    public RestaurantOut(Restaurant restaurant, Integer rating) {
        super(restaurant.getId(), restaurant.getName());
        this.address = restaurant.getAddress();
        this.rating = rating;
        this.menus = restaurant.getMenus();
    }

    public RestaurantOut(Integer id, String name, String address) {
        super(id, name);
        this.address = address;
        this.rating = null;
        this.menus = null;
    }
}