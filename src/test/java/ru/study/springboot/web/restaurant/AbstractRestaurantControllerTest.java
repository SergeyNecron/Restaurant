package ru.study.springboot.web.restaurant;

import org.springframework.test.web.servlet.ResultActions;
import ru.study.springboot.dto.RestaurantIn;
import ru.study.springboot.model.User;
import ru.study.springboot.web.AbstractControllerTest;

import java.time.LocalDate;

import static ru.study.springboot.web.restaurant.AdminRestaurantRestController.REST_URL_RESTAURANT_ADMIN;
import static ru.study.springboot.web.restaurant.UserRestaurantRestController.REST_URL_RESTAURANT_USER;

public abstract class AbstractRestaurantControllerTest extends AbstractControllerTest {

    @Override
    protected ResultActions getMvcResultGet(User user, String url) throws Exception {
        return super.getMvcResultGet(user, REST_URL_RESTAURANT_USER + "/" + url);
    }

    protected ResultActions getMvcResultGet(User user) throws Exception {
        return getMvcResultGet(user, "");
    }

    protected ResultActions getMvcResultGet(User user, Integer id) throws Exception {
        return getMvcResultGet(user, "" + id);
    }

    protected ResultActions getMvcResultGet(User user, LocalDate date) throws Exception {
        return super.getMvcResultGet(user, REST_URL_RESTAURANT_ADMIN + "/" + date);
    }

    protected ResultActions getMvcResultPost(User user, RestaurantIn restaurantIn) throws Exception {
        return super.getMvcResultPost(user, REST_URL_RESTAURANT_ADMIN, restaurantIn);
    }

    protected ResultActions getMvcResultPut(User user, Integer id, RestaurantIn restaurantIn) throws Exception {
        return super.getMvcResultPut(user, REST_URL_RESTAURANT_ADMIN + "/" + id, restaurantIn);
    }

    protected ResultActions getMvcResultDelete(User user, Integer id) throws Exception {
        return super.getMvcResultDelete(user, REST_URL_RESTAURANT_ADMIN + "/" + id);
    }
}
