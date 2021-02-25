package ru.study.springboot.web.profile;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.study.springboot.AuthUser;
import ru.study.springboot.model.User;
import ru.study.springboot.repository.UserRepository;
import ru.study.springboot.util.ValidationUtil;

import javax.validation.Valid;

@RestController
@RequestMapping(value = UserProfileRestController.REST_URL_PROFILE_USER, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
@Api(tags = "User profile controller")
public class UserProfileRestController {

    static final String REST_URL_PROFILE_USER = "/rest/user/profile";
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<User> get(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get {}", authUser);
        return ResponseEntity.of(userRepository.findById(authUser.id()));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        log.info("delete {}", authUser);
        userRepository.deleteById(authUser.id());
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody User user, @AuthenticationPrincipal AuthUser authUser) {
        log.info("update {} to {}", authUser, user);
        User oldUser = authUser.getUser();
        ValidationUtil.assureIdConsistent(user, oldUser.id());
        user.setRoles(oldUser.getRoles());
        if (user.getPassword() == null) {
            user.setPassword(oldUser.getPassword());
        }
        userRepository.save(user);
    }
}
