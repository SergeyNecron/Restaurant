package ru.study.springboot.web.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import ru.study.springboot.to.RestaurantIn;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.study.springboot.web.TestData.USER;
import static ru.study.springboot.web.TestData.USER_NOT_REGISTRATION;

class UserRestaurantRestControllerTest extends AbstractRestaurantControllerTest {

    @Test
    void getAllRestaurantsWithMenuNowUser() throws Exception {
        getAllRestaurantsWithMenuNow(USER);
    }

    @Test
    void createRestaurantForUserFailed() throws Exception {
        RestaurantIn restaurant = new RestaurantIn("newRestaurant");
        ResultActions action = getMvcResultPost(USER, restaurant);
        action.andExpect(status().isForbidden());
    }

    @Test
    void getUnAuth() throws Exception {
        getMvcResultGet(USER_NOT_REGISTRATION)
                .andExpect(status().isUnauthorized());
    }

    @Test
    void votingUnAuth() throws Exception {
        getMvcResultGet(USER_NOT_REGISTRATION, 2)
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createUnAuth() throws Exception {
        getMvcResultPost(USER_NOT_REGISTRATION, new RestaurantIn("newRestaurant"))
                .andExpect(status().isUnauthorized());
    }
}