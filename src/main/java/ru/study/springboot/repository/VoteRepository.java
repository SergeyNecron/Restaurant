package ru.study.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.study.springboot.model.User;
import ru.study.springboot.model.Vote;

import java.time.LocalDate;

@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Integer> {
    boolean existsByDateAndUser(LocalDate date, User user);

    @Query("SELECT count (v) FROM Vote v WHERE v.menu.id=:menuId and v.date=:date")
    Integer getVotingMenuByDate(int menuId, LocalDate date);

}