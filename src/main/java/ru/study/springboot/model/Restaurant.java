package ru.study.springboot.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurant", uniqueConstraints = {@UniqueConstraint(columnNames = "name", name = "restaurant_unique_name_idx")})
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends AbstractNamedEntity {

    private String address;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    @JsonManagedReference
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Menu> menus;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @JsonManagedReference(value = "restaurant-vote")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Vote> votes = new ArrayList<>();

    public Restaurant(String name) {
        super(name);
    }

    public Restaurant(String name, List<Menu> menus) {
        super(name);
        this.menus = menus;
    }

    public Restaurant(Integer id, String name, Menu menu) {
        super(id, name);
        this.menus = List.of(menu);
    }

    public Restaurant(String name, String address) {
        super(name);
        this.address = address;
    }

    @Override
    public String toString() {
        return "Restaurant {" +
                "  id = " + id +
                ", name = " + name +
                ", address = " + address +
                ", menus = " + menus +
                '}';
    }

    public void addMenus(Menu menu) {
        if (menus == null) menus = List.of(menu);
        else menus.add(menu);
    }
}
