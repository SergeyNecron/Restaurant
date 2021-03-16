package ru.study.springboot.web.profile;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import ru.study.springboot.dto.UserIn;
import ru.study.springboot.dto.UserOut;
import ru.study.springboot.error.IllegalRequestDataException;
import ru.study.springboot.model.User;
import ru.study.springboot.repository.UserRepository;

import java.util.List;
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

    public User create(UserIn userIn) {
        checkNotDuplicate(userRepository.getByEmail(userIn.getEmail()), userIn.getEmail());
        return userRepository.save(userIn.toUser());
    }

    public void update(UserIn userIn, Integer id) {
        log.info("update user {} ", userIn);
        User userOld = userRepository.getExisted(id);
        User user = userIn.updateUserFromUserDto(userOld);
        userRepository.save(user);
    }

    public void delete(int id) {
        log.info("delete user id = {}", id);
        if (id == 1) throw new IllegalRequestDataException("must not delete admin with id = 1");
        userRepository.deleteById(id);
    }
}