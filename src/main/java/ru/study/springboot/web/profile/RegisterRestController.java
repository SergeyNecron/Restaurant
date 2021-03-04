package ru.study.springboot.web.profile;

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
import ru.study.springboot.model.User;

import javax.validation.Valid;
import java.net.URI;

import static ru.study.springboot.web.profile.UserProfileRestController.REST_URL_PROFILE_USER;

@RestController
@RequestMapping(value = RegisterRestController.REST_URL_REGISTER, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
@Api(tags = "Register controller")
public class RegisterRestController extends AbstractProfileController {
    static final String REST_URL_REGISTER = "/rest/register";

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<UserOut> register(@Valid @RequestBody UserIn userIn) {
        User created = create(userIn);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL_PROFILE_USER)
                .build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(new UserOut(created));
    }
}