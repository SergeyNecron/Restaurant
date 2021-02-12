package ru.study.springboot.web.menu;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.study.springboot.error.IllegalRequestDataException;
import ru.study.springboot.model.Menu;
import ru.study.springboot.model.Restaurant;
import ru.study.springboot.repository.MenuRepository;
import ru.study.springboot.repository.RestaurantRepository;
import ru.study.springboot.to.MenuIn;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.study.springboot.util.ValidationUtil.*;

@RestController
@RequestMapping(value = AdminMenuRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
@Api(tags = "Admin menu controller")
public class AdminMenuRestController {
    static final String REST_URL = "/rest/admin/menu";
    static final Integer MIN_COUNT_MEALS_FOR_MENU = 2;
    static final Integer MAX_COUNT_MEALS_FOR_MENU = 5;

    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Menu> getMenuWithMeals(@PathVariable int id) {
        log.info("get menu {}", id);
        return ResponseEntity.of(menuRepository.findById(id));
    }

    @GetMapping
    public List<Menu> getAllMenusWithMeals() {
        log.info("getAll");
        return menuRepository.findAll();
    }

    @GetMapping("/date")
    public List<Menu> getAllMenusWithMealsByDate(@Param("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Get menus by date {}", date);
        return menuRepository.findAllByDate(date);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Menu> createMenuWithMeals(@Valid @RequestBody MenuIn menuIn) {
        log.info("create menu: {}", menuIn.getName());
        Menu menu = menuIn.toMenu();
        checkNew(menu);
        Integer restaurant_id = menuIn.getRestaurantId();
        Restaurant restaurant = checkNotFoundWithId(restaurantRepository.getById(restaurant_id), restaurant_id);
        checkCountMealsValid(menu);
        menu.setRestaurant(restaurant);
        Menu created = menuRepository.save(menu);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMenuWithMeals(@PathVariable int id) {
        log.info("delete {}", id);
        checkSingleModification(menuRepository.delete(id), "Menu id=" + id + " missed");
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMenuWithMeals(@Valid @RequestBody Menu menu, @PathVariable int id) {
        log.info("update {}", menu);
        assureIdConsistent(menu, id); // menu.id == id ?
        checkNotFoundWithId(menuRepository.findById(id), id);
        Integer restaurant_id = menu.getRestaurant().getId();
        checkNotFoundWithId(restaurantRepository.getById(restaurant_id), restaurant_id);
        menuRepository.save(menu);
    }

    private void checkCountMealsValid(Menu menu) {
        if (menu.getMeals().size() > MAX_COUNT_MEALS_FOR_MENU) throw new
                IllegalRequestDataException("Max count meals for menu = " + MAX_COUNT_MEALS_FOR_MENU);
        if (menu.getMeals().size() < MIN_COUNT_MEALS_FOR_MENU) throw new
                IllegalRequestDataException("Min count meals for menu = " + MIN_COUNT_MEALS_FOR_MENU);
    }
}