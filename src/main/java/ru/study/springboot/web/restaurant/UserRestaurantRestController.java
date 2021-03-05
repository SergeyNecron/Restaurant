package ru.study.springboot.web.restaurant;

import io.swagger.annotations.Api;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.study.springboot.dto.RestaurantOut;

import java.time.LocalDate;
import java.util.List;

@RestController
@Api(tags = "User restaurant controller")
@CacheConfig(cacheNames = "restaurants")
@RequestMapping(value = UserRestaurantRestController.REST_URL_RESTAURANT_USER)
public class UserRestaurantRestController extends AbstractRestaurantController {
    static final String REST_URL_RESTAURANT_USER = "/rest/user/restaurant";

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantOut> getRestaurantWithRatingAndMenusNow(@PathVariable Integer id) {
        return ResponseEntity.ok(get(id));
    }

    @GetMapping
    @Cacheable
    public List<RestaurantOut> getAllRestaurantsWithMenuNow() {
        return getAll(LocalDate.now());
    }
}
