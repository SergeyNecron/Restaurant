package ru.study.springboot.dto;

import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.study.springboot.model.Meal;
import ru.study.springboot.model.Menu;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
public class MenuIn extends BaseIn {

    @NotNull
    LocalDate date;
    @NotNull
    Integer restaurantId;
    @NotNull
    List<Meal> meals;

    public MenuIn(String name, LocalDate date, Integer restaurantId, List<Meal> meals) {
        setName(name);
        this.date = date;
        this.restaurantId = restaurantId;
        this.meals = meals;
    }

    public Menu toMenu() {
        return new Menu(null, getName(), date, meals);
    }
}