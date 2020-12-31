package ru.study.springboot.model;

import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"password"})
public class User extends AbstractBaseEntity {

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "role"}, name = "user_roles_unique")})
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    Set<Role> roles;

    @Column(name = "vail")
    @Range(min = 0, max = 100000)
    Integer vail;

    @Column(name = "email", nullable = false, unique = true)
    @Email
    @NotEmpty
    @Size(max = 128)
    private String email;

    @Column(name = "password")
    @Size(max = 256)
    private String password;

    public User(String email, String password, Role role, Role... roles) {
        this.email = email;
        this.password = password;
        this.roles = EnumSet.of(role, roles);
    }
}
