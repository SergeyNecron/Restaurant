package ru.study.springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseOut {

    @NotNull
    private Integer id;

    @NotNull
    private String name;
}