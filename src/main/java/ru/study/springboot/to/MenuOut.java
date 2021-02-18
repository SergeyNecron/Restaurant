package ru.study.springboot.to;

import lombok.*;
import ru.study.springboot.model.AbstractNamedEntity;
import ru.study.springboot.model.Meal;
import ru.study.springboot.model.Menu;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor // ! if no create: JsonUtil.java:24 (like default constructor, exist)
public class MenuOut extends AbstractNamedEntity {
    @NotNull
    LocalDate date;
    @NotNull
    Integer restaurantId;
    @NotNull
    List<Meal> meals;

    public MenuOut(Menu menu) {
        this.id = menu.id();
        this.name = menu.getName();
        this.date = menu.getDate();
        this.restaurantId = menu.getRestaurant().id();
        this.meals = menu.getMeals();
    }

    public MenuOut(MenuIn menuIn) {
        this.id = 0;
        this.name = menuIn.getName();
        this.date = menuIn.getDate();
        this.restaurantId = menuIn.getRestaurantId();
        this.meals = menuIn.getMeals();
    }
}