package ru.study.springboot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.study.springboot.model.Role;
import ru.study.springboot.model.User;
import ru.study.springboot.util.JsonDeserializers;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.EnumSet;
import java.util.Set;

@Value
@EqualsAndHashCode(callSuper = true)
public class UserIn extends BaseIn {
    @Email
    @NotEmpty
    @Size(max = 128)
    String email;

    @NotEmpty
    @Size(max = 256)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonDeserialize(using = JsonDeserializers.PasswordDeserializer.class)
    String password;

    Set<Role> roles;

    public UserIn(String email, String password, Role role, Role... roles) {
        this.email = email;
        this.password = password;
        this.roles = EnumSet.of(role, roles);
    }

    public User toUser() {
        return new User(email, password, Role.USER);
    }
}