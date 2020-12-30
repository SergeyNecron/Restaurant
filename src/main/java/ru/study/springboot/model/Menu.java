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
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "menus")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, exclude = {"user"})
public class Menu extends AbstractBaseEntity{

    public Menu (Integer id, String saloon, Map<String, Double> meals) {
        super(id);
        this.saloon = saloon;
        this.meals = meals;
        this.dateCreateMenu = LocalDateTime.now().toLocalDate();
    }

    @Column(name = "saloon", nullable = false)
    @NotBlank
    @Size(min = 2, max = 120)
    String saloon;

    @Column(name = "date", nullable = false)
    @NotNull
    LocalDate dateCreateMenu;

    @CollectionTable(name = "meals", joinColumns = @JoinColumn(name = "id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"id", "meal"}, name = "menu_meals_unique")})
    @Column(name = "meal")
    @ElementCollection(fetch = FetchType.EAGER)
    Map<String, Double> meals = new HashMap();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;
}
