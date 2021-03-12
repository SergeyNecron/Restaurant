package ru.study.springboot.repository;

import org.springframework.transaction.annotation.Transactional;
import ru.study.springboot.model.User;
import ru.study.springboot.model.Vote;

import java.time.LocalDate;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

    Optional<Vote> getVoteByDateAndUser(LocalDate date, User user);

}