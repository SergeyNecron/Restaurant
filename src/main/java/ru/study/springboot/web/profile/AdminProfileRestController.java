package ru.study.springboot.web.profile;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.study.springboot.dto.UserIn;
import ru.study.springboot.dto.UserOut;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = AdminProfileRestController.REST_URL_PROFILE_ADMIN, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
@Api(tags = "Admin profile controller")
public class AdminProfileRestController extends AbstractProfileController {
    static final String REST_URL_PROFILE_ADMIN = "/rest/admin/profile";

    @GetMapping("/{id}")
    public ResponseEntity<UserOut> getUser(@PathVariable int id) {
        return ResponseEntity.ok(get(id));
    }

    @GetMapping
    public List<UserOut> getAllUsers() {
        return getAll();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUser(@Valid @RequestBody UserIn userIn, @PathVariable Integer id) {
        update(userIn, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable int id) {
        delete(id);
    }
}