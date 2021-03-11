package ru.study.springboot.web.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import ru.study.springboot.dto.RestaurantOut;
import ru.study.springboot.model.User;
import ru.study.springboot.web.TestUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.study.springboot.web.TestData.*;
import static ru.study.springboot.web.TestUtil.getTestRestaurantsTo;

class UserRestaurantRestControllerTest extends AbstractRestaurantControllerTest {

    @Test
    void getRestaurantWithRatingAndMenusNowForUser() throws Exception {
        get(USER, 1);
    }

    @Test
    void getRestaurantWithRatingAndMenusNowForAdmin() throws Exception {
        get(ADMIN, 1);
    }

    @Test
    void getRestaurantWithRatingAndMenusNowForUnAuthFailed() throws Exception {
        getMvcResultGet(USER_NOT_REGISTRATION, 1)
                .andExpect(status().isUnauthorized());
    }

    private void get(User user, Integer id) throws Exception {
        MvcResult action = getMvcResultGet(user, id)
                .andExpect(status().isOk())
                .andReturn();
        RestaurantOut restaurantsActual = TestUtil.readFromJsonMvcResult(action, RestaurantOut.class);
        assertEquals(new RestaurantOut(restaurant1, 0), restaurantsActual);
    }

    @Test
    void getAllRestaurantsWithMenuNowUser() throws Exception {
        getAll(USER);
    }

    @Test
    void getAllRestaurantsWithMenuNowForAdmin() throws Exception {
        getAll(ADMIN);
    }

    @Test
    void getAllRestaurantsWithMenuNowForUnAuthFailed() throws Exception {
        getMvcResultGet(USER_NOT_REGISTRATION)
                .andExpect(status().isUnauthorized());
    }

    private void getAll(User user) throws Exception {
        MvcResult action = getMvcResultGet(user)
                .andExpect(status().isOk())
                .andReturn();
        List<RestaurantOut> restaurantsActual = TestUtil.readListFromJsonMvcResult(action, RestaurantOut.class);
        assertEquals(getTestRestaurantsTo(), restaurantsActual);
    }

}