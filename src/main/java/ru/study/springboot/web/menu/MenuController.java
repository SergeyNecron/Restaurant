package ru.study.springboot.web.menu;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.study.springboot.error.IllegalRequestDataException;
import ru.study.springboot.model.User;
import ru.study.springboot.repository.UserRepository;
import ru.study.springboot.to.MenuTo;
import ru.study.springboot.model.Menu;
import ru.study.springboot.repository.MenuRepository;
import ru.study.springboot.util.Util;

import javax.validation.Valid;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.study.springboot.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = "/menu", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class MenuController {
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;
    static final Integer MAX_COUNT_MENU = 6;

    @PostMapping(value = "/create",consumes = MediaType.APPLICATION_JSON_VALUE)
    public Menu create(@Valid @RequestBody Menu menu) {
        log.info("create menu{}", menu);
        checkNew(menu);
        menu.setDateCreateMenu(LocalDate.now());
        Integer countMenu = menuRepository.countByDate(LocalDate.now());
       if(countMenu<MAX_COUNT_MENU) return menuRepository.save(menu);
       else throw new IllegalRequestDataException("Max count menu = " + MAX_COUNT_MENU);
    }

    @GetMapping
    public List<MenuTo> getAll() {
        log.info("getAll menus");
        final List<Menu> allMenu = menuRepository.findAllByDate(LocalDate.now());
        return allMenu.stream().map(this::getTo).collect(Collectors.toList());
    }

    @GetMapping("/voting")
    public User voting(Integer userId, Integer menuId) {
        log.info("voting user {}", userId);
        User user =  userRepository.findById(userId).orElseThrow();
        user.setVail(menuId);
        return userRepository.save(user);
    }

    private MenuTo getTo(Menu menu) {
        Integer rating = userRepository.getVotingMenu(menu.id());
        return Util.to(menu, rating);
    }
}
