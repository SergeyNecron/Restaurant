package ru.study.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.study.springboot.model.Menu;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface MenuRepository extends JpaRepository<Menu, Integer> {

    @Query("SELECT m FROM Menu m WHERE m.date=:date")
    List<Menu> findAllByDate(LocalDate date);

    @Transactional
    @Modifying
    @Query("DELETE FROM Menu m WHERE m.id=:id")
    int delete(@Param("id") int id);
}