package ru.study.springboot.web.restaurant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.study.springboot.error.NotFoundException;
import ru.study.springboot.model.Restaurant;
import ru.study.springboot.repository.RestaurantRepository;
import ru.study.springboot.repository.VoteRepository;
import ru.study.springboot.to.RestaurantIn;
import ru.study.springboot.to.RestaurantOut;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static ru.study.springboot.util.ValidationUtil.*;

@Slf4j
public abstract class AbstractRestaurantController {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private VoteRepository voteRepository;

    public RestaurantOut get(Integer id) {
        log.info("get restaurant id = {}, with rating and menus", id);
        return toRatingRestaurant(restaurantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("RestaurantOut id=" + id + " not found")));
    }

    public List<RestaurantOut> getAll(LocalDate date) {
        log.info("get all restaurants with rating and menus by date {}", date);
        return restaurantRepository.getAllRestaurantsWithMenuOnDate(date)
                .stream()
                .map(this::toRatingRestaurant)
                .collect(Collectors.toList());
    }

    public Restaurant create(Restaurant restaurant) {
        log.info("create restaurant: {}", restaurant);
        checkNotDuplicate(restaurantRepository.getByName(restaurant.getName()), restaurant.getName());
        return restaurantRepository.save(restaurant);
    }

    public void update(RestaurantIn restaurantIn, int id) {
        log.info("update restaurant: {}", restaurantIn);
        assureIdConsistent(restaurantIn, id); // restaurant.id == id ?
        Restaurant restaurant = checkNotFoundWithId(restaurantRepository.getById(id), id);
        restaurant.setName(restaurantIn.getName());
        restaurant.setAddress(restaurantIn.getAddress());
        restaurantRepository.save(restaurant);
        log.info("Restaurant id =" + id + " has been update");
    }

    public void delete(int id) {
        log.info("delete {}", id);
        restaurantRepository.deleteById(id);
        log.info("Restaurant id =" + id + " has been deleted");
    }

    private RestaurantOut toRatingRestaurant(Restaurant restaurant) {
        Integer rating = voteRepository.getCountVoteByDateForRestaurant(restaurant.id(), LocalDate.now());
        return new RestaurantOut(restaurant, rating);
    }
}