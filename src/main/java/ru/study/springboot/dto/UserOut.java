package ru.study.springboot.dto;

import lombok.*;
import ru.study.springboot.model.Role;
import ru.study.springboot.model.User;

import java.util.Set;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true, exclude = "roles")
@NoArgsConstructor // ! if no create: JsonUtil.java:24 (like default constructor, exist)
public class UserOut extends BaseOut {

    String email;
    @Setter
    Set<Role> roles;

    public UserOut(User user) {
        super(user.id(), user.getName());
        this.email = user.getEmail();
        this.roles = user.getRoles();
    }

    public UserOut(UserIn userIn, Integer userId) {
        super(userId, userIn.getName());
        this.email = userIn.getEmail();
        this.roles = userIn.getRoles();
    }
}