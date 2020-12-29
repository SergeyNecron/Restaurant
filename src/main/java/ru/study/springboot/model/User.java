package ru.study.springboot.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class User extends AbstractBaseEntity {
    UserRole role = UserRole.USER;
    Integer vail;
    List<Menu> list = new ArrayList();
}
