package ru.study.springboot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @JsonDeserialize(using = JsonDeserializers.PasswordDeserializer.class)
    private String password;

    public UserIn(String name, String email, String password) {
        this.setName(name);
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email.toLowerCase();
    }

    public UserIn(User user) {
        this(user.getName(), user.getEmail(), user.getPassword());
    }

    public User toUser() {
        return new User(getName(), email.toLowerCase(), password,
                Set.of(Role.USER)); // при создании всегда роль User);
    }

    public User updateUserFromUserDto(User user) {
        user.setName(getName());
        user.setEmail(email.toLowerCase());
        user.setPassword(password);
        if (roles != null) user.setRoles(roles);
        return user;
    }

    @Setter
    private Set<Role> roles;

    @Override
    public String toString() {
        return "UserIn{" +
                " name = " + email +
                ", email = " + email +
                ", password = " + password +
                ", roles = " + roles +
                '}';
    }
}