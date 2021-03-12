package ru.study.springboot.web.profile;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import ru.study.springboot.dto.UserIn;
import ru.study.springboot.dto.UserOut;
import ru.study.springboot.error.IllegalRequestDataException;
import ru.study.springboot.error.NotFoundException;
import ru.study.springboot.model.Role;
import ru.study.springboot.model.User;
import ru.study.springboot.repository.UserRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.study.springboot.util.ValidationUtil.checkNotDuplicate;

@Slf4j
public abstract class AbstractProfileController {

    @Autowired
    private UserRepository userRepository;

    public UserOut get(int id) {
        log.info("get user id = {}", id);
        return new UserOut(userRepository.getExisted(id));
    }

    public List<UserOut> getAll() {
        log.info("get all users");
        return userRepository
                .findAll(Sort.by(Sort.Direction.ASC, "email"))
                .stream()
                .map(UserOut::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public User create(UserIn userIn) {
        checkNotDuplicate(userRepository.getByEmail(userIn.getEmail()), userIn.getEmail());
        User user = new User();
        userIn.setRoles(Set.of(Role.USER));// при создании всегда роль User
        updateUserFromUserDto(user, userIn);
        return userRepository.save(user);
    }

    @Transactional
    public void update(UserIn userIn, Integer id) {
        log.info("update user {} ", userIn);
        User user = userRepository.getExisted(id);
        updateUserFromUserDto(user, userIn);
        userRepository.save(user);
        log.info("User id = " + id + " has been update");
    }

    @Transactional
    public void delete(int id) {
        log.info("delete user id = {}", id);
        if (id == 1) throw new IllegalRequestDataException("must not delete admin with id = 1");
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("restaurant id = " + id + " not found");
        }
        log.info("Menu id = " + id + " has been deleted");
        log.info("User id = " + id + " has been deleted");
    }

    private void updateUserFromUserDto(User user, UserIn userIn) {
        user.setName(userIn.getName());
        user.setEmail(userIn.getEmail());
        if (userIn.getRoles() != null)
            user.setRoles(userIn.getRoles());
        if (userIn.getPassword() != null) {
            user.setPassword(userIn.getPassword());
        }
    }
}