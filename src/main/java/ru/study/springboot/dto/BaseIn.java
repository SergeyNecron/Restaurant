package ru.study.springboot.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public abstract class BaseIn {
    @NotNull
    private String name;
}