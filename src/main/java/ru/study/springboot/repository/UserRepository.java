package ru.study.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.study.springboot.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}