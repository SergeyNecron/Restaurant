package ru.study.springboot.service;

import org.hibernate.Hibernate;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.study.springboot.dto.RestaurantIn;
import ru.study.springboot.error.NotFoundException;
import ru.study.springboot.model.Menu;
import ru.study.springboot.model.Restaurant;
import ru.study.springboot.repository.RestaurantRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static ru.study.springboot.util.ValidationUtil.checkNotDuplicate;
import static ru.study.springboot.util.ValidationUtil.checkNotFoundWithId;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional
    public Optional<Restaurant> get(Integer id) {
        final Optional<Restaurant> restaurant = restaurantRepository.findById(id);
        restaurant.ifPresent(value -> {
            ifMenuIsPresetThenInitializeMenu(value);
            Hibernate.initialize(value.getVotes());
        });
        return restaurant;
    }

    @Transactional
    public List<Restaurant> getAll(LocalDate date) {
        List<Restaurant> restaurants = restaurantRepository.getAllRestaurantsWithMenuOnDate(date);
        if (restaurants.size() != 0) {
            ifMenuIsPresetThenInitializeMenu(restaurants.get(0));
            Hibernate.initialize(restaurants.get(0).getVotes());
        }
        return restaurants;
    }

    @Transactional
    public List<Restaurant> getAll() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        if (restaurants.size() != 0) {
            Hibernate.initialize(restaurants.get(0).getVotes());
        }
        return restaurants;
    }

    private void ifMenuIsPresetThenInitializeMenu(Restaurant restaurants) {
        List<Menu> menus = restaurants.getMenus();
        if (menus.size() != 0) Hibernate.initialize(menus.get(0).getMeals());
    }

    public Restaurant create(Restaurant restaurant) {
        checkNotDuplicate(restaurantRepository.getByName(restaurant.getName()), restaurant.getName());
        return restaurantRepository.save(restaurant);
    }

    public void update(RestaurantIn restaurantIn, int id) {
        Restaurant restaurant = checkNotFoundWithId(restaurantRepository.findById(id), id);
        restaurant.setName(restaurantIn.getName());
        restaurant.setAddress(restaurantIn.getAddress());
        restaurantRepository.save(restaurant);
    }

    public void delete(int id) {
        try {
            restaurantRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("restaurant id = " + id + " not found");
        }
    }
}