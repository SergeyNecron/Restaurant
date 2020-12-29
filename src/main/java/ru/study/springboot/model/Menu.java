package ru.study.springboot.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "menus", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id"}, name = "menus_unique_user_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, exclude = {"user"})
public class Menu extends AbstractBaseEntity{

    public Menu (Integer id, String saloon,  List<Meal> meals) {
        super(id);
        this.saloon = saloon;
        this.meals = meals;
        this.dateCreateMenu =  LocalDateTime.now().toLocalDate();
    }

    @Column(name = "saloon", nullable = false)
    @NotBlank
    @Size(min = 2, max = 120)
    String saloon;

    @Column(name = "date", nullable = false)
    @NotNull
    LocalDate dateCreateMenu;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menu")//, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonManagedReference
    @OnDelete(action = OnDeleteAction.CASCADE) //https://stackoverflow.com/a/44988100/548473
    List<Meal> meals = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;
}
