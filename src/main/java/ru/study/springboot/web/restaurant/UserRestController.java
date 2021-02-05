package ru.study.springboot.web.restaurant;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.study.springboot.AuthUser;
import ru.study.springboot.error.IllegalRequestDataException;
import ru.study.springboot.error.NotFoundException;
import ru.study.springboot.model.Menu;
import ru.study.springboot.model.Restaurant;
import ru.study.springboot.model.User;
import ru.study.springboot.model.Vote;
import ru.study.springboot.repository.RestaurantRepository;
import ru.study.springboot.repository.VoteRepository;
import ru.study.springboot.to.RestaurantOut;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.study.springboot.util.ValidationUtil.checkNotFoundWithId;

@RestController
@RequestMapping(value = UserRestController.REST_URL_RESTAURANT_USER)
@Slf4j
@AllArgsConstructor
@Api(tags = "User Restaurant Controller")
public class UserRestController {
    public static final String REST_URL_RESTAURANT_USER = "/rest/user/restaurant/";
    public static final String GET_ALL = "/getAll";
    static final Integer MIN_COUNT_MEALS_FOR_MENU = 2;
    static final Integer MAX_COUNT_MEALS_FOR_MENU = 5;
    static final LocalTime endTime = LocalTime.of(11, 0);

    private final VoteRepository votingRepository;
    private final RestaurantRepository restaurantRepository;

    @GetMapping("/{name}")
    public RestaurantOut getRestaurantsWithMenuToDay(@PathVariable String name) {
        return getRestaurantWithMenuOnDate(name, LocalDate.now());
    }

    @GetMapping("/date/{name}")
    public RestaurantOut getRestaurantWithMenuOnDate(
            @PathVariable String name, @Param("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("get Restaurant {} on date {}", name, date);
        Restaurant restaurant = restaurantRepository.findRestaurantWithMenuByDate(name
                , date
        )
                .orElseThrow(() -> new NotFoundException("Restaurant id=" + name + " not found"));
        return toRatingRestaurant(restaurant);
    }

    @GetMapping(GET_ALL)
    public List<RestaurantOut> getAllRestaurantsWithMenuToDay() {
        return getAllRestaurantsWithMenuOnDate(LocalDate.now());
    }

    @GetMapping(GET_ALL + "/date/{date}")
    public List<RestaurantOut> getAllRestaurantsWithMenuOnDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Get restaurants by date {}", date);
        return restaurantRepository.getAllRestaurantsWithMenuOnDate(
                date
        )
                .stream()
                .map(this::toRatingRestaurant)
                .collect(Collectors.toList());
    }

    @PutMapping("/{restaurant_id}")
    public RestaurantOut vote(@AuthenticationPrincipal AuthUser authUser, @PathVariable int restaurant_id) {
        User user = authUser.getUser();
        Restaurant restaurant = checkNotFoundWithId(restaurantRepository.getById(restaurant_id), restaurant_id);
        log.info("user id = {} voting restaurant =  {}", user.id(), restaurant.getName());
        checkReVote(LocalDateTime.now(), user);
        votingRepository.save(new Vote(LocalDate.now(), user, restaurant));
        return getRestaurantsWithMenuToDay(restaurant.getName());
    }

    private void checkReVote(LocalDateTime timeReVote, User user) {
        if (timeReVote.toLocalTime().isAfter(endTime)
                && (votingRepository.existsByDateAndUser(timeReVote.toLocalDate(), user))
        )
            throw new IllegalRequestDataException("you cannot re-vote after: " + endTime);
    }

    private RestaurantOut toRatingRestaurant(Restaurant restaurant) {
        Integer rating = votingRepository.getCountVoteByDateForRestaurant(restaurant.getId(), LocalDate.now());
        return new RestaurantOut(restaurant
                , rating
        );
    }

    private void checkCountMeals(List<Menu> menus) {
        for (Menu menu : menus) {
            if (menu.getMeals().size() > MAX_COUNT_MEALS_FOR_MENU) throw new
                    IllegalRequestDataException("Max count meals for menu = " + MAX_COUNT_MEALS_FOR_MENU);
            if (menu.getMeals().size() < MIN_COUNT_MEALS_FOR_MENU) throw new
                    IllegalRequestDataException("Min count meals for menu = " + MIN_COUNT_MEALS_FOR_MENU);
        }
    }
}
