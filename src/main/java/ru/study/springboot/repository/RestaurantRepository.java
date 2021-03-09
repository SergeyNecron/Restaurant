package ru.study.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.study.springboot.model.Restaurant;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

    Optional<Restaurant> getByName(String name);

    @Query("SELECT distinct r FROM Restaurant r " +
            "LEFT JOIN FETCH r.menus m " +
            "WHERE m.date = :date")
    List<Restaurant> getAllRestaurantsWithMenuOnDate(LocalDate date);
}