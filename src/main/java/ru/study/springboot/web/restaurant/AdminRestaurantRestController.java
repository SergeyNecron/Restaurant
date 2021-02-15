package ru.study.springboot.web.restaurant;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.study.springboot.model.Restaurant;
import ru.study.springboot.to.RestaurantIn;
import ru.study.springboot.to.RestaurantOut;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
@Api(tags = "Admin restaurant controller")
@RequestMapping(value = AdminRestaurantRestController.REST_URL_RESTAURANT_ADMIN)
public class AdminRestaurantRestController extends AbstractRestaurantController {
    static final String REST_URL_RESTAURANT_ADMIN = "/rest/admin/restaurant";

    @GetMapping("/date/{id}")
    public ResponseEntity<RestaurantOut> getRestaurantWithRatingAndMenusByDate(
            @PathVariable Integer id, @Param("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(super.get(id, date));
    }

    @GetMapping("/date")
    public List<RestaurantOut> getAllRestaurantsWithMenuByDate(
            @Param("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return super.getAll(date);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> createRestaurant(@Valid @RequestBody RestaurantIn restaurantIn) {
        Restaurant created = super.create(restaurantIn.toRestaurant());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL_RESTAURANT_ADMIN + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRestaurantWithMenusWithMeals(@PathVariable int id) {
        super.delete(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateRestaurant(@Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
        super.update(restaurant, id);
    }
}
