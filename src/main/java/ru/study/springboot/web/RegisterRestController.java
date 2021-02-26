package ru.study.springboot.web;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.study.springboot.dto.UserIn;
import ru.study.springboot.dto.UserOut;
import ru.study.springboot.model.Role;
import ru.study.springboot.model.User;
import ru.study.springboot.repository.UserRepository;

import javax.validation.Valid;
import java.net.URI;
import java.util.Set;

import static ru.study.springboot.util.ValidationUtil.checkNotDuplicate;

@RestController
@RequestMapping(value = RegisterRestController.REST_URL_REGISTER, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
@Api(tags = "Register controller")
public class RegisterRestController {
    static final String REST_URL_REGISTER = "/rest/register";
    private final UserRepository userRepository;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<UserOut> register(@Valid @RequestBody UserIn userIn) {
        log.info("register {}", userIn);
        checkNotDuplicate(userRepository.getByEmail(userIn.getEmail()), userIn.getEmail());
        User user = userIn.toUser();
        user.setRoles(Set.of(Role.USER));
        user = userRepository.save(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/account")
                .build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(new UserOut(user));
    }
}