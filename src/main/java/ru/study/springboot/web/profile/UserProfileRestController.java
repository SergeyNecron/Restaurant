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
import ru.study.springboot.dto.UserIn;
import ru.study.springboot.dto.UserOut;
import ru.study.springboot.model.Role;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping(value = UserProfileRestController.REST_URL_PROFILE_USER, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
@Api(tags = "User profile controller")
public class UserProfileRestController extends AbstractProfileController {

    static final String REST_URL_PROFILE_USER = "/rest/user/profile";

    @GetMapping
    public ResponseEntity<UserOut> get(@AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok(get(authUser.id()));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody UserIn userIn, @AuthenticationPrincipal AuthUser authUser) {
        final Set<Role> userRoles = authUser.getUser().getRoles();
        if (!userRoles.contains(Role.ADMIN))
            userIn.setRoles(userRoles); // User не может поменять свою роль, только админ
        update(userIn, authUser.id());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        delete(authUser.id());
    }
}
