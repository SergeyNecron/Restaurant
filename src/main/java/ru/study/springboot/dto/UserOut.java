package ru.study.springboot.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.study.springboot.model.Role;
import ru.study.springboot.model.User;

import java.util.Set;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor // ! if no create: JsonUtil.java:24 (like default constructor, exist)
public class UserOut extends BaseOut {

    String email;
    String password;
    Set<Role> roles;

    public UserOut(User user) {
        super(user.id(), user.getName());
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.roles = user.getRoles();
    }
}