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

    Optional<Restaurant> getById(Integer id);

    @Query("SELECT count (r) FROM Restaurant r " +
            "LEFT JOIN r.votes v " +
            "WHERE v.date =:date " +
            "GROUP BY r.id, r.name ")
    Integer countByDate(LocalDate date);

    @Query("SELECT r FROM Restaurant r "
            +
            "LEFT JOIN FETCH r.menus m " +
            "WHERE r.name = :name "
            +
            "AND m.date = :date"
    )
    Optional<Restaurant> findRestaurantWithMenuByDate(String name
            , LocalDate date
    );

    @Query("SELECT r FROM Restaurant r "
            +
            "LEFT JOIN FETCH r.menus m " +
            "WHERE m.date = :date"
    )
    List<Restaurant> getAllRestaurantsWithMenuOnDate(
            LocalDate date
    );
}