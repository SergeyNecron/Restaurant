package ru.study.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.study.springboot.model.Restaurant;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

    Optional<Restaurant> getByName(String name);

    Optional<Restaurant> getById(Integer id);

    @Query("SELECT r FROM Restaurant r " +
            "LEFT JOIN FETCH r.menus m " +
            "WHERE r.id = :id " +
            "AND m.date = :date")
    Optional<Restaurant> findRestaurantWithMenuByDate(Integer id, LocalDate date);

    @Query("SELECT r FROM Restaurant r " +
            "LEFT JOIN FETCH r.menus m " +
            "WHERE m.date = :date")
    List<Restaurant> getAllRestaurantsWithMenuOnDate(LocalDate date);

    @Transactional
    @Modifying
    @Query("DELETE FROM Restaurant r WHERE r.id=:id")
    int delete(@Param("id") int id);
}