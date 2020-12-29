package ru.study.springboot.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
abstract class AbstractBaseEntity {
    private Integer id;
}
