package ru.study.springboot.web.menu;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.study.springboot.dto.MenuIn;
import ru.study.springboot.dto.MenuOut;
import ru.study.springboot.error.NotFoundException;
import ru.study.springboot.model.Menu;
import ru.study.springboot.model.Restaurant;
import ru.study.springboot.repository.MenuRepository;
import ru.study.springboot.repository.RestaurantRepository;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static ru.study.springboot.util.ValidationUtil.checkCountMealsValid;
import static ru.study.springboot.util.ValidationUtil.checkNotDuplicate;

@RestController
@RequestMapping(value = AdminMenuRestController.REST_URL_MENU_ADMIN, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
@Api(tags = "Admin menu controller")
@CacheConfig(cacheNames = {"restaurants", "menus", "menus-by-date", "restaurants-by-date"})
public class AdminMenuRestController {
    static final String REST_URL_MENU_ADMIN = "/rest/admin/menu";

    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    @GetMapping("/{id}")
    public ResponseEntity<MenuOut> getMenuWithMeals(@PathVariable int id) {
        log.info("get menu {}", id);
        return ResponseEntity.ok(new MenuOut(menuRepository.get(id)
                .orElseThrow(() -> new NotFoundException("restaurant id = " + id + " not found"))));
    }

    @GetMapping
    @Cacheable(cacheNames = "menus")
    public List<MenuOut> getAllMenusWithMeals() {
        log.info("getAll");
        return menuRepository
                .getAll()
                .stream()
                .map(MenuOut::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/date/{date}")
    @Cacheable(cacheNames = "menus-by-date")
    public List<MenuOut> getAllMenusWithMealsByDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("get menus by date {}", date);
        return menuRepository
                .getAllByDate(date)
                .stream()
                .map(MenuOut::new)
                .collect(Collectors.toList());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @CacheEvict(allEntries = true)
    public ResponseEntity<MenuOut> createMenuWithMeals(@Valid @RequestBody MenuIn menuIn) {
        log.info("create menu: {} for restaurant {}", menuIn.getName(), menuIn.getRestaurantId());
        checkCountMealsValid(menuIn);
        checkNotDuplicate(menuRepository.getMenuByDateAndNameAndRestaurant_Id(menuIn.getDate(), menuIn.getName(), menuIn.getRestaurantId()), "menu");

        Restaurant restaurant = restaurantRepository.getExisted(menuIn.getRestaurantId());
        final Menu menu = menuIn.toMenu(restaurant);
        MenuOut created = new MenuOut(menuRepository.save(menu));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL_MENU_ADMIN + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @CacheEvict(allEntries = true)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMenuWithMeals(@Valid @RequestBody MenuIn menuIn, @PathVariable Integer id) {
        log.info("update menu: {}", menuIn);
        checkCountMealsValid(menuIn);
        Menu menuOld = menuRepository.getExisted(id);
        Restaurant restaurant = restaurantRepository.getExisted(menuIn.getRestaurantId());
        Menu menu = menuIn.updateMenuFromMenuDto(menuOld, restaurant);
        menuRepository.save(menu);
    }

    @DeleteMapping("/{id}")
    @CacheEvict(allEntries = true)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMenuWithMeals(@PathVariable int id) {
        log.info("delete menu: {}", id);
        try {
            menuRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("restaurant id = " + id + " not found");
        }
    }

}