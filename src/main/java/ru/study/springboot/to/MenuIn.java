package ru.study.springboot.to;

import lombok.AllArgsConstructor;
import lombok.Value;
import ru.study.springboot.model.Meal;
import ru.study.springboot.model.Menu;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Value
@AllArgsConstructor
public class MenuIn {
    @NotNull
    String name;
    @NotNull
    LocalDate date;
    @NotNull
    Integer restaurantId;
    @NotNull
    List<Meal> meals;

    public Menu toMenu() {
        return new Menu(name, date, meals);
    }
}