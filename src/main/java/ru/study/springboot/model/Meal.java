package ru.study.springboot.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Meal extends AbstractBaseEntity {
    String name;
    Double price;
}
