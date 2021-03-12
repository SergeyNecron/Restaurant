package ru.study.springboot.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.study.springboot.model.Menu;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MenuRepository extends BaseRepository<Menu> {

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Menu m LEFT JOIN FETCH m.meals WHERE m.id = :id")
    Optional<Menu> get(int id);

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Menu m " +
            "LEFT JOIN FETCH m.meals " +
            "ORDER BY m.name ASC")
    List<Menu> getAll();

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Menu m " +
            "LEFT JOIN FETCH m.meals " +
            "WHERE m.date=:date " +
            "ORDER BY m.name ASC")
    List<Menu> getAllByDate(LocalDate date);

    Optional<Menu> getMenuByDateAndNameAndRestaurant_Id(LocalDate date, String name, Integer id);
}