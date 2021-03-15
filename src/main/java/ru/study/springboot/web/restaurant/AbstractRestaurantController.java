package ru.study.springboot.web.restaurant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.study.springboot.dto.RestaurantIn;
import ru.study.springboot.dto.RestaurantOut;
import ru.study.springboot.model.Restaurant;
import ru.study.springboot.service.RestaurantService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractRestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    public RestaurantOut get(Integer id) {
        log.info("get restaurant id = {}, with rating and menus", id);
        return new RestaurantOut(restaurantService.get(id));
    }

    public List<RestaurantOut> getAll() {
        log.info("get all restaurants with rating and menus");
        return restaurantService.getAll()
                .stream()
                .map(RestaurantOut::new)
                .collect(Collectors.toList());
    }

    public List<RestaurantOut> getAll(LocalDate date) {
        log.info("get all restaurants with rating and menus by date {}", date);
        return restaurantService.getAll(date)
                .stream()
                .map(RestaurantOut::new)
                .collect(Collectors.toList());
    }

    public Restaurant create(Restaurant restaurant) {
        log.info("create restaurant: {}", restaurant);
        return restaurantService.create(restaurant);
    }

    public void update(RestaurantIn restaurantIn, int id) {
        log.info("update restaurant: {}", restaurantIn);
        restaurantService.update(restaurantIn, id);
    }

    public void delete(int id) {
        log.info("delete restaurant {}", id);
        restaurantService.delete(id);

    }
}