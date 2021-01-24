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
import ru.study.springboot.model.Menu;
import ru.study.springboot.model.User;
import ru.study.springboot.model.Vote;
import ru.study.springboot.repository.MenuRepository;
import ru.study.springboot.repository.UserRepository;
import ru.study.springboot.repository.VoteRepository;
import ru.study.springboot.to.MenuTo;
import ru.study.springboot.to.VoteTo;
import ru.study.springboot.util.MenuUtil;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.study.springboot.util.ValidationUtil.*;

@RestController
@RequestMapping(value = MenuRestController.REST_URL_MENU)
@Slf4j
@AllArgsConstructor
@Api(tags="Menu Controller")
public class MenuRestController {
    static final String REST_URL_MENU = "/api/menu/";
    static final String GET_MENU = "get";
    static final String CREATE = "create";
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;
    private final VoteRepository votingRepository;

    static final Integer MAX_COUNT_MENU = 5;
    static final LocalTime endTime = LocalTime.of(11,0);

    @PostMapping(value = CREATE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public MenuTo create(@Valid @RequestBody MenuTo menuTo) {
        Menu menu = MenuUtil.toMenu(menuTo);
        log.info("create menu{}", menu);
        checkNew(menu);
        checkNotDuplicate(menuRepository.getBySaloon(menu.getSaloon()), menu.getSaloon());
        checkNotMoreMaxCount();
        menu.setDateCreateMenu(LocalDate.now());
        return toRatingMenu(menuRepository.save(menu));
    }

    @GetMapping(GET_MENU)
    public List<MenuTo> getAll() {
        log.info("getAll menus");
        return menuRepository
                .findAllByDate(LocalDate.now())
                .stream()
                .map(this::toRatingMenu)
                .collect(Collectors.toList());
    }

    @PutMapping("/{saloon}")
    public VoteTo voting(@AuthenticationPrincipal AuthUser authUser, @PathVariable String saloon) {
        final int id = authUser.id();
        log.info("voting user {}", id);
        User user = checkNotFoundWithId(userRepository.findById(id), id);
        checkReVote(LocalDateTime.now(), user);
        final Menu menu = checkNotFoundWithName(menuRepository.getBySaloon(saloon), saloon);
        return new VoteTo(votingRepository.save(new Vote(LocalDate.now(), user, menu)));
    }

    private void checkReVote(LocalDateTime timeReVote, User user) {
        if (timeReVote.toLocalTime().isAfter(endTime)
                && (votingRepository.existsByDateAndUser(timeReVote.toLocalDate(), user))
        )
            throw new IllegalRequestDataException("you cannot re-vote after: " + endTime);
    }

    private MenuTo toRatingMenu(Menu menu) {
        Integer rating = votingRepository.getVotingMenuByDate(menu.id(), LocalDate.now());
        return MenuUtil.to(menu, rating);
    }

    private void checkNotMoreMaxCount() {
        Integer countMenu = menuRepository.countByDate(LocalDate.now());
        if (countMenu >= MAX_COUNT_MENU) throw new
                IllegalRequestDataException("Max count menu = " + MAX_COUNT_MENU);
    }
}
