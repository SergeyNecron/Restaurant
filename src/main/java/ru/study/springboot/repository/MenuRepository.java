package ru.study.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.study.springboot.model.Menu;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
}