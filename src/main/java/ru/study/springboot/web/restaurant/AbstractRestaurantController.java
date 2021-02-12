package ru.study.springboot.web.restaurant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.study.springboot.error.NotFoundException;
import ru.study.springboot.model.Restaurant;
import ru.study.springboot.repository.RestaurantRepository;
import ru.study.springboot.repository.VoteRepository;
import ru.study.springboot.to.RestaurantOut;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static ru.study.springboot.util.ValidationUtil.checkNotDuplicate;

@Slf4j
public abstract class AbstractRestaurantController {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private VoteRepository voteRepository;

    public Restaurant create(Restaurant restaurant) {
        log.info("create restaurant: {}", restaurant);
        checkNotDuplicate(restaurantRepository.getByName(restaurant.getName()), restaurant.getName());
        return restaurantRepository.save(restaurant);
    }

    public RestaurantOut get(String name, LocalDate date) {
        log.info("get restaurant {} with rating and menus by date {} ", name, date);
        return toRatingRestaurant(restaurantRepository.findRestaurantWithMenuByDate(name, date)
                .orElseThrow(() -> new NotFoundException("RestaurantOut id=" + name + " not found")));
    }

    public List<RestaurantOut> getAll(LocalDate date) {
        log.info("get all restaurants with rating and menus by date {}", date);
        return restaurantRepository.getAllRestaurantsWithMenuOnDate(date)
                .stream()
                .map(this::toRatingRestaurant)
                .collect(Collectors.toList());
    }

    private RestaurantOut toRatingRestaurant(Restaurant restaurant) {
        Integer rating = voteRepository.getCountVoteByDateForRestaurant(restaurant.id(), LocalDate.now());
        return new RestaurantOut(restaurant, rating);
    }
}