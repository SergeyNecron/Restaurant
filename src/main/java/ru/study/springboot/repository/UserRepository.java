package ru.study.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.study.springboot.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT count (u) FROM User u WHERE u.vail=:id")
    Integer getVotingMenu(int id);
}