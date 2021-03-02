package ru.study.springboot.service;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.study.springboot.dto.RestaurantIn;
import ru.study.springboot.model.Restaurant;
import ru.study.springboot.repository.RestaurantRepository;
import ru.study.springboot.repository.VoteRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static ru.study.springboot.util.ValidationUtil.checkNotDuplicate;
import static ru.study.springboot.util.ValidationUtil.checkNotFoundWithId;

@Service
public class RestaurantService {

    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, VoteRepository voteRepository) {
        this.restaurantRepository = restaurantRepository;
        this.voteRepository = voteRepository;
    }

    @Transactional
    public Optional<Restaurant> get(Integer id) {
        final Optional<Restaurant> restaurant = restaurantRepository.findById(id);
        restaurant.ifPresent(value -> {
            Hibernate.initialize(value.getMenus().get(0).getMeals());
            Hibernate.initialize(value.getVotes());
        });
        return restaurant;
    }

    @Transactional
    public List<Restaurant> getAll(LocalDate date) {
        List<Restaurant> restaurants = restaurantRepository.getAllRestaurantsWithMenuOnDate(date);
        Hibernate.initialize(restaurants.get(0).getMenus().get(0).getMeals());
        Hibernate.initialize(restaurants.get(0).getVotes());
        return restaurants;
    }

    public Restaurant create(Restaurant restaurant) {
        checkNotDuplicate(restaurantRepository.getByName(restaurant.getName()), restaurant.getName());
        return restaurantRepository.save(restaurant);
    }

    public void update(RestaurantIn restaurantIn, int id) {
        Restaurant restaurant = checkNotFoundWithId(restaurantRepository.getById(id), id);
        restaurant.setName(restaurantIn.getName());
        restaurant.setAddress(restaurantIn.getAddress());
        restaurantRepository.save(restaurant);
    }

    public void delete(int id) {
        restaurantRepository.deleteById(id);
    }
}