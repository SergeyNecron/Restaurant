package ru.study.springboot.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends AbstractNamedEntity {

    private String address;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "restaurant")
    // restaurant-menu https://stackoverflow.com/questions/20119142/
    @JsonManagedReference(value = "restaurant-menu")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Menu> menus;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @JsonManagedReference(value = "restaurant-vote")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Vote> votes = new ArrayList<>();

    public Restaurant(Integer id, String name, List<Menu> menus) {
        super(id, name);
        this.menus = menus;
    }

    public Restaurant(Integer id, String name, Menu menu) {
        super(id, name);
        this.menus = List.of(menu);
    }

    public Restaurant(Integer id, String name, String address) {
        super(id, name);
        this.address = address;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "  id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", menus=" + menus +
                '}';
    }
}
