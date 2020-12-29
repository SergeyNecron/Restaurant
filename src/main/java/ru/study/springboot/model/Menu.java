package ru.study.springboot.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Menu extends AbstractBaseEntity{
    String nameSaloon;
    LocalDate dateCreateMenu = LocalDateTime.now().toLocalDate();
    List<Meal> meals = new ArrayList<>();
}
