package ru.study.springboot.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "menus")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends AbstractBaseEntity{

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menu")//, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonManagedReference
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Vote> votes;

    public Menu(String saloon, Map<String, Double> meals) {
        this.saloon = saloon;
        this.meals = meals;
    }
}
