package ru.study.springboot.web.restaurant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.study.springboot.dto.RestaurantIn;
import ru.study.springboot.dto.RestaurantOut;
import ru.study.springboot.error.NotFoundException;
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
        final Restaurant restaurant = restaurantService.get(id)
                .orElseThrow(() -> new NotFoundException("RestaurantOut id = " + id + " not found"));
        return new RestaurantOut(restaurant, getRatingRestaurantByDateNow(restaurant));
    }

    public List<RestaurantOut> getAll() {
        log.info("get all restaurants with rating and menus");
        return restaurantService.getAll()
                .stream()
                .map(it -> new RestaurantOut(it, getRatingRestaurantByDateNow(it)))
                .collect(Collectors.toList());
    }

    public List<RestaurantOut> getAll(LocalDate date) {
        log.info("get all restaurants with rating and menus by date {}", date);
        return restaurantService.getAll(date)
                .stream()
                .map(it -> new RestaurantOut(it, getRatingRestaurantByDateNow(it)))
                .collect(Collectors.toList());
    }

    public Restaurant create(Restaurant restaurant) {
        log.info("create restaurant: {}", restaurant);
        return restaurantService.create(restaurant);
    }

    public void update(RestaurantIn restaurantIn, int id) {
        log.info("update restaurant: {}", restaurantIn);
        restaurantService.update(restaurantIn, id);
        log.info("Restaurant id = " + id + " has been update");
    }

    public void delete(int id) {
        log.info("delete {}", id);
        restaurantService.delete(id);
        log.info("Restaurant id = " + id + " has been deleted");
    }

    private int getRatingRestaurantByDateNow(Restaurant restaurant) {
        return (int) restaurant.getVotes()
                .stream()
                .filter(it -> it.getDate().equals(LocalDate.now()))
                .count();
    }
}