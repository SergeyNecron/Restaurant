package ru.study.springboot.web.restaurant;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.study.springboot.dto.RestaurantIn;
import ru.study.springboot.dto.RestaurantOut;
import ru.study.springboot.model.Restaurant;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.study.springboot.web.restaurant.UserRestaurantRestController.REST_URL_RESTAURANT_USER;

@RestController
@AllArgsConstructor
@CacheConfig(cacheNames = "restaurants")
@Api(tags = "Admin restaurant controller")
@RequestMapping(value = AdminRestaurantRestController.REST_URL_RESTAURANT_ADMIN)
public class AdminRestaurantRestController extends AbstractRestaurantController {
    static final String REST_URL_RESTAURANT_ADMIN = "/rest/admin/restaurant";

    @GetMapping("/{date}")
    @Cacheable
    public List<RestaurantOut> getAllRestaurantsWithMenuByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return getAll(date);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(allEntries = true)
    public ResponseEntity<Restaurant> createRestaurant(@Valid @RequestBody RestaurantIn restaurantIn) {
        Restaurant created = create(restaurantIn.toRestaurant());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL_RESTAURANT_USER + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(allEntries = true)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateRestaurant(@Valid @RequestBody RestaurantIn restaurantIn, @PathVariable int id) {
        update(restaurantIn, id);
    }

    @DeleteMapping("/{id}")
    @CacheEvict(allEntries = true)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRestaurantWithMenusWithMeals(@PathVariable int id) {
        delete(id);
    }
}
