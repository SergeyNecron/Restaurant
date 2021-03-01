package ru.study.springboot.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, exclude = {"menu"})
public class Meal extends AbstractNamedEntity {

    @Column(name = "price", nullable = false)
    @NotNull
    @Range(min = 0, max = 100000)
    Double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    @JsonBackReference
    @NotNull
    private Menu menu;

    public Meal(String name, Double price) {
        super(null, name);
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Meal)) return false;
        if (!super.equals(o)) return false;
        Meal meal = (Meal) o;
        return Objects.equals(getPrice(), meal.getPrice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getPrice());
    }

}