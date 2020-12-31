package ru.study.springboot.model;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "menus")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends AbstractBaseEntity{

    public Menu (String saloon, Map<String, Double> meals) {
        this.saloon = saloon;
        this.meals = meals;
    }

    @Column(name = "saloon", nullable = false)
    @NotBlank
    @Size(min = 2, max = 120)
    private String saloon;

    @Column(name = "date", nullable = false)
    private LocalDate dateCreateMenu = LocalDate.now();

    @CollectionTable(name = "meals", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "meal")
    @ElementCollection(fetch = FetchType.EAGER)
    private Map<String, Double> meals = new HashMap();
}
