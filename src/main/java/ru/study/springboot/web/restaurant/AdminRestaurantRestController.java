package ru.study.springboot.web.restaurant;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> create(@Valid @RequestBody RestaurantIn restaurantIn) {
        Restaurant created = super.create(restaurantIn.toRestaurant());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL_RESTAURANT_ADMIN + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping("/date/{name}")
    public ResponseEntity<RestaurantOut> getRestaurantWithRatingAndMenusByDate(@PathVariable String name,
                                                                               @Param("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(super.get(name, date));
    }

    @GetMapping("/date")
    public List<RestaurantOut> getAllRestaurantsWithMenuByDate(
            @Param("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return super.getAll(date);
    }
}
