package ru.study.springboot.web.menu;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.study.springboot.dto.MenuIn;
import ru.study.springboot.dto.MenuOut;
import ru.study.springboot.error.IllegalRequestDataException;
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

import static ru.study.springboot.util.ValidationUtil.checkNotFoundWithId;

@RestController
@RequestMapping(value = AdminMenuRestController.REST_URL_MENU_ADMIN, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
@Api(tags = "Admin menu controller")
public class AdminMenuRestController {
    static final String REST_URL_MENU_ADMIN = "/rest/admin/menu";
    static final Integer MIN_COUNT_MEALS_FOR_MENU = 2;
    static final Integer MAX_COUNT_MEALS_FOR_MENU = 5;

    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    @GetMapping("/{id}")
    public ResponseEntity<MenuOut> getMenuWithMeals(@PathVariable int id) {
        log.info("get menu {}", id);
        return ResponseEntity.ok(new MenuOut(menuRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("restaurant id= " + id + " not found"))));
    }

    @GetMapping
    public List<MenuOut> getAllMenusWithMeals() {
        log.info("getAll");
        return menuRepository
                .findAll()
                .stream()
                .map(MenuOut::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/date/{date}")
    public List<MenuOut> getAllMenusWithMealsByDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("get menus by date {}", date);
        return menuRepository
                .findAllByDate(date)
                .stream()
                .map(MenuOut::new)
                .collect(Collectors.toList());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<MenuOut> createMenuWithMeals(@Valid @RequestBody MenuIn menuIn) {
        log.info("create menu: {} for restaurant {}", menuIn.getName(), menuIn.getRestaurantId());
        final Menu menu = buildMenu(menuIn);
        MenuOut created = new MenuOut(menuRepository.save(menu));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL_MENU_ADMIN + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMenuWithMeals(@Valid @RequestBody MenuIn menuIn, @PathVariable Integer id) {
        log.info("update menu: {}", menuIn);
        checkNotFoundWithId(menuRepository.findById(id), id);
        final Menu menu = buildMenu(menuIn);
        menu.setId(id);
        menuRepository.save(menu);
    }

    @DeleteMapping("/{id}")
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMenuWithMeals(@PathVariable int id) {
        log.info("delete menu: {}", id);
        menuRepository.deleteById(id);
        log.info("Menu id = " + id + " has been deleted");
    }

    private Menu buildMenu(MenuIn menuIn) {
        final Menu menu = menuIn.toMenu();
        checkCountMealsValid(menu);
        Restaurant restaurant = checkNotFoundWithId(restaurantRepository.findById(menuIn.getRestaurantId()), menuIn.getRestaurantId());
        menu.setRestaurant(restaurant);
        menu.getMeals().forEach(it -> it.setMenu(menu));
        return menu;
    }

    private void checkCountMealsValid(Menu menu) {
        if (menu.getMeals().size() > MAX_COUNT_MEALS_FOR_MENU) throw new
                IllegalRequestDataException("Max count meals for menu = " + MAX_COUNT_MEALS_FOR_MENU);
        if (menu.getMeals().size() < MIN_COUNT_MEALS_FOR_MENU) throw new
                IllegalRequestDataException("Min count meals for menu = " + MIN_COUNT_MEALS_FOR_MENU);
    }
}