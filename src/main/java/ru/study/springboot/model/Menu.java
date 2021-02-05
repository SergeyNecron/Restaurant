package ru.study.springboot.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "menu")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends AbstractNamedEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonBackReference(value = "restaurant-menu")
    private Restaurant restaurant;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "menu")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Meal> meals;

    public Menu(String name) {
        super.name = name;
        this.date = LocalDate.now();
    }

    public Menu(String name, List<Meal> meals) {
        super.name = name;
        this.meals = meals;
        this.date = LocalDate.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Menu)) return false;
        if (!super.equals(o)) return false;
        Menu menu = (Menu) o;
        return Objects.equals(getDate(), menu.getDate()) &&
                Objects.equals(getMeals(), menu.getMeals());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getDate(), getMeals());
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", date=" + date +
                ", meals=" + meals +
                '}';
    }
}
