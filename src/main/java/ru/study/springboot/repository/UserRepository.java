package ru.study.springboot.repository;

import org.springframework.transaction.annotation.Transactional;
import ru.study.springboot.model.User;

import java.util.Optional;

@Transactional(readOnly = true)
public interface UserRepository extends BaseRepository<User> {

    Optional<User> getByEmail(String email);
}