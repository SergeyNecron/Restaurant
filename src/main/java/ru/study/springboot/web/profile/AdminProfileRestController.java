package ru.study.springboot.web.profile;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.study.springboot.dto.UserIn;
import ru.study.springboot.error.IllegalRequestDataException;
import ru.study.springboot.model.User;
import ru.study.springboot.repository.UserRepository;

import javax.validation.Valid;
import java.util.List;

import static ru.study.springboot.util.ValidationUtil.checkNotDuplicate;
import static ru.study.springboot.util.ValidationUtil.checkNotFoundWithId;

@RestController
@RequestMapping(value = AdminProfileRestController.REST_URL_PROFILE_ADMIN, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
@Api(tags = "Admin profile controller")
public class AdminProfileRestController {

    static final String REST_URL_PROFILE_ADMIN = "/rest/admin/profile";
    private final UserRepository userRepository;

    @GetMapping("/{id}")
    public ResponseEntity<User> get(@PathVariable int id) {
        log.info("get {}", id);
        return ResponseEntity.of(userRepository.findById(id));
    }

    @GetMapping
    public List<User> getAll() {
        log.info("getAll");
        return userRepository.findAll();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody UserIn userIn, @PathVariable Integer id) {
        log.info("update {} ", userIn);
        checkNotDuplicate(userRepository.getByEmail(userIn.getEmail()), userIn.getEmail());
        User user = checkNotFoundWithId(userRepository.findById(id), id);
        user.setEmail(userIn.getEmail());
        user.setRoles(userIn.getRoles());
        if (user.getPassword() != null) {
            user.setPassword(userIn.getPassword());
        }
        userRepository.save(user);
    }

    @DeleteMapping("/{id}")
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete user id = {}", id);
        if (id == 1) throw new IllegalRequestDataException("must not delete admin with id = 1");
        userRepository.deleteById(id);
        log.info("User id =" + id + " has been deleted");
    }
}