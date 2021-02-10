package ru.study.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.study.springboot.model.User;
import ru.study.springboot.model.Vote;

import java.time.LocalDate;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Integer> {

    Optional<Vote> getVoteByDateAndUser(LocalDate date, User user);

    @Query("SELECT count (v) FROM Vote v WHERE v.restaurant.id=:restaurantId and v.date=:date")
    Integer getCountVoteByDateForRestaurant(int restaurantId, LocalDate date);

}