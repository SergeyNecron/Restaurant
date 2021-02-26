package ru.study.springboot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.study.springboot.model.Role;
import ru.study.springboot.model.User;
import ru.study.springboot.util.JsonDeserializers;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@NoArgsConstructor
public class UserIn extends BaseIn {
    @Email
    @NotEmpty
    @Size(max = 128)
    private String email;

    @NotEmpty
    @Size(max = 256)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonDeserialize(using = JsonDeserializers.PasswordDeserializer.class)
    private String password;

    private Set<Role> roles;

    public User toUser() {
        return new User(getName(), email, password, roles);
    }
}