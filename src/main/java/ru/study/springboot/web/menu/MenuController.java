package ru.study.springboot.web.menu;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.study.springboot.model.Menu;
import ru.study.springboot.repository.MenuRepository;
import ru.study.springboot.repository.UserRepository;

import javax.validation.Valid;

import java.util.List;

import static ru.study.springboot.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = "/menu", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class MenuController {

    private final UserRepository userRepository;
    private final MenuRepository menuRepository;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Menu create(Integer userId, @Valid @RequestBody Menu meal) {
        log.info("create {} for user {}", meal, userId);
        checkNew(meal);
        meal.setUser(userRepository.getOne(userId));
        return menuRepository.save(meal);
    }

    @GetMapping
    public List<Menu> getAll(Integer userId) {
        log.info("getAll for user {}", userId);
        final List<Menu> all = menuRepository.findAll();
        return all;
    }
}
