package ru.study.springboot.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "vote", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "date"}, name = "unique_user_date_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Vote extends AbstractBaseEntity {

    @Column(name = "date", nullable = false)
    @NotNull
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference(value = "user-vote")
    @NotNull
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonBackReference(value = "restaurant-vote")
    @NotNull
    private Restaurant restaurant;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Vote votes = (Vote) o;
        return Objects.equals(getDate(), votes.getDate()) && Objects.equals(getUser().getId(), votes.getUser().getId())
                && Objects.equals(getRestaurant().getId(), votes.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getDate(), getUser(), getRestaurant());
    }

    @Override
    public String toString() {
        return "Vote{" +
                " id = " + id +
                ", date = " + date +
                ", user = " + user.id +
                ", menu = " + restaurant.id +
                '}';
    }
}
