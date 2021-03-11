package ru.study.springboot.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "menu", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "date", "restaurant_id"}, name = "unique_menu_date_restaurant_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends AbstractNamedEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonBackReference
    private Restaurant restaurant;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Meal> meals;

    public Menu(Integer id, String name, LocalDate date, List<Meal> meals) {
        super(id, name);
        this.meals = meals;
        this.date = date;
    }

    public Menu(String name, List<Meal> meals) {
        this(null, name, LocalDate.now(), meals);
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
                " id = " + id +
                ", name= " + name +
                ", date= " + date +
                ", meals= " + meals +
                '}';
    }
}
