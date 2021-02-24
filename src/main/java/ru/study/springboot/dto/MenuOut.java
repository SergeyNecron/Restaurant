package ru.study.springboot.to;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.study.springboot.model.Meal;
import ru.study.springboot.model.Menu;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor // ! if no create: JsonUtil.java:24 (like default constructor, exist)
public class MenuOut extends BaseOut {
    @NotNull
    LocalDate date;
    @NotNull
    Integer restaurantId;
    @NotNull
    List<Meal> meals;

    public MenuOut(Menu menu) {
        super(menu.getId(), menu.getName());
        this.date = menu.getDate();
        this.restaurantId = menu.getRestaurant().id();
        this.meals = menu.getMeals();
    }

    public MenuOut(MenuIn menuIn, Integer menuId) {
        super(menuId, menuIn.getName());
        this.date = menuIn.getDate();
        this.restaurantId = menuIn.getRestaurantId();
        this.meals = menuIn.getMeals();
    }
}