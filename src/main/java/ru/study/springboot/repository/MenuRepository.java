package ru.study.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.study.springboot.model.Menu;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MenuRepository extends JpaRepository<Menu, Integer> {

    @Query("SELECT count (m) FROM Menu m WHERE m.dateCreateMenu=:date")
    Integer countByDate(LocalDate date);

    @Query("SELECT m FROM Menu m WHERE m.dateCreateMenu=:date")
    List<Menu> findAllByDate(LocalDate date);

    Optional<Menu> getBySaloon(String saloon);
}