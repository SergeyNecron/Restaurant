package ru.study.springboot.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "meals", uniqueConstraints = {@UniqueConstraint(columnNames = {"menu_id"}, name = "meals_unique_user_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, exclude = {"menu"})
public class Meal extends AbstractBaseEntity {

    public Meal (Integer id, String name, Double price) {
        super(id);
        this.name = name;
        this.price = price;
    }

    @Column(name = "name", nullable = false)
    @NotBlank
    @Size(min = 2, max = 120)
    String name;

    @Column(name = "price", nullable = false)
    @NotNull
    @Range(min = 100, max = 10000)
    Double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    @JsonBackReference
    private Menu menu;
}
