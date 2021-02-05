package ru.study.springboot.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.study.springboot.model.Menu;
import ru.study.springboot.model.Restaurant;

import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantOut {
    private Integer id;
    private String name;
    private String address;
    private Integer rating;
    private List<Menu> menus;

    public RestaurantOut(Restaurant restaurant, Integer rating) {
        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.address = restaurant.getAddress();
        this.menus = restaurant.getMenus();
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantOut that = (RestaurantOut) o;
        return Objects.equals(getName(), that.getName()) &&
                Objects.equals(getAddress(), that.getAddress()) &&
                Objects.equals(getMenus(), that.getMenus()) &&
                Objects.equals(getRating(), that.getRating());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getAddress(), getRating());
    }
}