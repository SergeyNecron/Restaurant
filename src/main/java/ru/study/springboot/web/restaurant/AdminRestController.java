package ru.study.springboot.web.restaurant;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.study.springboot.model.Restaurant;
import ru.study.springboot.repository.RestaurantRepository;
import ru.study.springboot.to.RestaurantIn;

import javax.validation.Valid;
import java.net.URI;

import static ru.study.springboot.util.ValidationUtil.checkNotDuplicate;

@RestController
@RequestMapping(value = AdminRestController.REST_URL_RESTAURANT_ADMIN)
@Slf4j
@AllArgsConstructor
@Api(tags = "Admin Restaurant Controller")
public class AdminRestController {
    static final String REST_URL_RESTAURANT_ADMIN = "/rest/admin/restaurant/";
    private final RestaurantRepository restaurantRepository;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> create(@Valid @RequestBody RestaurantIn restaurantIn) {
        log.info("create restaurant name: {}", restaurantIn.getName());
        checkNotDuplicate(restaurantRepository.getByName(restaurantIn.getName()), restaurantIn.getName());
        Restaurant created = restaurantRepository.save(restaurantIn.toRestaurant());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL_RESTAURANT_ADMIN + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
