package ru.study.springboot.to;

import lombok.*;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuTo {
    String nameSaloon;
    Map<String, Double> meals;
    Integer rating;
}
