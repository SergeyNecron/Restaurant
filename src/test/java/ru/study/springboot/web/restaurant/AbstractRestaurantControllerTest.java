package ru.study.springboot.web.restaurant;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import ru.study.springboot.model.User;
import ru.study.springboot.to.RestaurantIn;
import ru.study.springboot.to.RestaurantOut;
import ru.study.springboot.web.AbstractControllerTest;
import ru.study.springboot.web.TestUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.study.springboot.web.TestUtil.getTestRestaurantsTo;
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

    protected ResultActions getMvcResultPost(User user, RestaurantIn restaurantIn) throws Exception {
        return super.getMvcResultPost(user, REST_URL_RESTAURANT_ADMIN, restaurantIn);
    }

    protected void getAllRestaurantsWithMenuNow(User user) throws Exception {
        MvcResult action = getMvcResultGet(user)
                .andExpect(status().isOk())
                .andReturn();
        List<RestaurantOut> restaurantsActual = TestUtil.readListFromJsonMvcResult(action, RestaurantOut.class);
        assertEquals(getTestRestaurantsTo(), restaurantsActual);
    }
}
