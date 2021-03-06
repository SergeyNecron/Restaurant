package ru.study.springboot.web.profile;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
import java.util.List;

@RestController
@RequestMapping(value = AdminProfileRestController.REST_URL_PROFILE_ADMIN, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
@CacheConfig(cacheNames = "users")
@Api(tags = "Admin profile controller")
public class AdminProfileRestController extends AbstractProfileController {
    static final String REST_URL_PROFILE_ADMIN = "/rest/admin/profile";

    @GetMapping("/{id}")
    public ResponseEntity<UserOut> getUser(@PathVariable int id) {
        return ResponseEntity.ok(get(id));
    }

    @GetMapping
    @Cacheable
    public List<UserOut> getAllUsers() {
        return getAll();
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(allEntries = true)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<UserOut> createUser(@Valid @RequestBody UserIn userIn) {
        log.info("create {}", userIn);
        User created = create(userIn);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL_PROFILE_ADMIN + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(new UserOut(created));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(allEntries = true)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUser(@Valid @RequestBody UserIn userIn, @PathVariable Integer id) {
        update(userIn, id);
    }

    @DeleteMapping("/{id}")
    @CacheEvict(allEntries = true)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable int id) {
        delete(id);
    }
}