package ru.study.springboot.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, exclude = "menu")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
        super(name);
        this.price = price;
    }

    @Override
    public String toString() {
        return "Meal{" +
                " name = " + name +
                ", price = " + price +
                '}';
    }
}