package ru.study.springboot.web;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.study.springboot.AuthUser;
import ru.study.springboot.error.IllegalRequestDataException;
import ru.study.springboot.model.User;
import ru.study.springboot.repository.UserRepository;
import ru.study.springboot.to.MenuRating;
import ru.study.springboot.model.Menu;
import ru.study.springboot.repository.MenuRepository;
import ru.study.springboot.util.MenuUtil;
import static ru.study.springboot.util.ValidationUtil.*;
import static ru.study.springboot.util.ValidationUtil.checkNotDuplicate;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.study.springboot.util.MenuUtil.isBetweenHalfOpen;


@RestController
@RequestMapping(value = MenuRestController.REST_URL_MENU)
@Slf4j
@AllArgsConstructor
@Api(tags="Menu Controller")
public class MenuRestController {
    static final String REST_URL_MENU = "/api/menu/";
    static final String GET_MENU= "get";
    static final String CREATE = "create";
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;

    static final Integer MAX_COUNT_MENU = 5;
    static final LocalTime startTime = LocalTime.of(0,0);
    static final LocalTime endTime = LocalTime.of(11,0);

    @PostMapping(value = CREATE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public MenuRating create(@Valid @RequestBody Menu menu) {
        log.info("create menu{}", menu);
        checkNew(menu);
        checkNotDuplicate(menuRepository.getBySaloon(menu.getSaloon()), menu.getSaloon());
        checkNotMoreMaxCount();
        menu.setDateCreateMenu(LocalDate.now());
        return toRatingMenu(menuRepository.save(menu));
    }

    @GetMapping(GET_MENU)
    public List<MenuRating> getAll() {
        log.info("getAll menus");
        return menuRepository
                .findAllByDate(LocalDate.now())
                .stream()
                .map(this::toRatingMenu)
                .collect(Collectors.toList());
    }

    @PutMapping("/{saloon}")
    public User voting(@AuthenticationPrincipal AuthUser authUser, @PathVariable String saloon) {
        final int id = authUser.id();
        log.info("voting user {}", id);
        User user = checkNotFoundWithId(userRepository.findById(id), id);
        checkReVote(LocalTime.now(), user);
        final Menu menu = checkNotFoundWithName(menuRepository.getBySaloon(saloon), saloon);
        user.setVail(menu.id());
        return userRepository.save(user);
    }

    private void checkReVote(LocalTime timeReVote, User user) {
        if  (!isBetweenHalfOpen(timeReVote, startTime, endTime) && (user.getVail() != null) && (user.getVail() != 0))
            throw new IllegalRequestDataException("you cannot re-vote after: " + endTime);
    }

    private MenuRating toRatingMenu(Menu menu) {
        Integer rating = userRepository.getVotingMenu(menu.id());
        return MenuUtil.to(menu, rating);
    }

    private void checkNotMoreMaxCount() {
        Integer countMenu = menuRepository.countByDate(LocalDate.now());
        if(countMenu>=MAX_COUNT_MENU) throw new
                IllegalRequestDataException("Max count menu = " + MAX_COUNT_MENU);
    }
}
