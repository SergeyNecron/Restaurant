package ru.study.springboot.to;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.study.springboot.model.AbstractBaseEntity;
import ru.study.springboot.model.Meal;
import ru.study.springboot.model.Menu;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MenuIn extends AbstractBaseEntity {
    @NotNull
    String name;
    @NotNull
    LocalDate date;
    @NotNull
    Integer restaurantId;
    @NotNull
    List<Meal> meals;

    public Menu toMenu() {
        return new Menu(id, name, date, meals);
    }
}