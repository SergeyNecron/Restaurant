package ru.study.springboot.web.restaurant;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.study.springboot.error.NotFoundException;
import ru.study.springboot.model.Restaurant;
import ru.study.springboot.repository.RestaurantRepository;
import ru.study.springboot.repository.VoteRepository;
import ru.study.springboot.to.RestaurantOut;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = UserRestaurantRestController.REST_URL_RESTAURANT_USER)
@Slf4j
@AllArgsConstructor
@Api(tags = "User Restaurant Controller")
public class UserRestaurantRestController {
    public static final String REST_URL_RESTAURANT_USER = "/rest/user/restaurant";

    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;

    @GetMapping("/{name}")
    public ResponseEntity<RestaurantOut> getRestaurantsWithMenuToDay(@PathVariable String name) {
        return getRestaurantWithMenuOnDate(name, LocalDate.now());
    }

    @GetMapping("/date/{name}")
    public ResponseEntity<RestaurantOut> getRestaurantWithMenuOnDate(
            @PathVariable String name, @Param("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("get Restaurant {} on date {}", name, date);
        Restaurant restaurant = restaurantRepository.findRestaurantWithMenuByDate(name, date)
                .orElseThrow(() -> new NotFoundException("Restaurant id=" + name + " not found"));
        return ResponseEntity.ok(toRatingRestaurant(restaurant));
    }

    @GetMapping
    public List<RestaurantOut> getAllRestaurantsWithMenuToDay() {
        return getAllRestaurantsWithMenuOnDate(LocalDate.now());
    }

    @GetMapping("/date")
    public List<RestaurantOut> getAllRestaurantsWithMenuOnDate(
            @Param("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Get restaurants by date {}", date);
        return restaurantRepository.getAllRestaurantsWithMenuOnDate(
                date
        )
                .stream()
                .map(this::toRatingRestaurant)
                .collect(Collectors.toList());
    }

    private RestaurantOut toRatingRestaurant(Restaurant restaurant) {
        Integer rating = voteRepository.getCountVoteByDateForRestaurant(restaurant.id(), LocalDate.now());
        return new RestaurantOut(restaurant, rating);
    }
}
