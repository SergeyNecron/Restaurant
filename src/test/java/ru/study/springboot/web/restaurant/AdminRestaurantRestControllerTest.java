package ru.study.springboot.web.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import ru.study.springboot.dto.RestaurantIn;
import ru.study.springboot.dto.RestaurantOut;
import ru.study.springboot.model.Restaurant;
import ru.study.springboot.web.TestUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.study.springboot.web.TestData.*;
import static ru.study.springboot.web.TestUtil.getTestRestaurantsTo;

class AdminRestaurantRestControllerTest extends AbstractRestaurantControllerTest {

    @Test
    void getAllRestaurantsWithMenuByDateForUserFailed() throws Exception {
        getMvcResultGet(USER, LocalDate.now())
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllRestaurantsWithMenuByDateForAdmin() throws Exception {
        MvcResult action = getMvcResultGet(ADMIN, LocalDate.now())
                .andExpect(status().isOk())
                .andReturn();
        List<RestaurantOut> restaurantsActual = TestUtil.readListFromJsonMvcResult(action, RestaurantOut.class);
        assertEquals(getTestRestaurantsTo(), restaurantsActual);
    }

    @Test
    void getAllRestaurantsWithMenuByErrorDateForUserFailed() throws Exception {
        MvcResult action = getMvcResultGet(ADMIN, LocalDate.MIN)
                .andExpect(status().isOk())
                .andReturn();
        List<RestaurantOut> restaurantsActual = TestUtil.readListFromJsonMvcResult(action, RestaurantOut.class);
        assertEquals(new ArrayList<>(), restaurantsActual);
    }

    @Test
    void getAllRestaurantsWithMenuByDateForUnAuthFailed() throws Exception {
        getMvcResultGet(USER_NOT_REGISTRATION, LocalDate.now())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createRestaurantForUserFailed() throws Exception {
        getMvcResultPost(USER, new RestaurantIn("newRestaurant"))
                .andExpect(status().isForbidden());
    }

    @Test
    void createRestaurantForAdmin() throws Exception {
        RestaurantIn restaurantIn = new RestaurantIn("newRestaurant");
        ResultActions action = getMvcResultPost(ADMIN, restaurantIn);
        action.andExpect(status().isCreated());
        RestaurantOut actualRestaurant = TestUtil.readFromJson(action, RestaurantOut.class);
        RestaurantOut restaurantOut = new RestaurantOut(5, restaurantIn.getName(), restaurantIn.getAddress());
        assertEquals(restaurantOut, actualRestaurant);
    }

    @Test
    void createRestaurantForUnAuthFailed() throws Exception {
        getMvcResultPost(USER_NOT_REGISTRATION, new RestaurantIn("newRestaurant"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void checkDuplicateCreateForAdminFailed() throws Exception {
        getMvcResultPost(ADMIN, new RestaurantIn("София"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateRestaurantForUserFailed() throws Exception {
        getMvcResultPut(USER, 1, new RestaurantIn("newRestaurant"))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateRestaurantForAdmin() throws Exception {
        RestaurantIn restaurantIn = new RestaurantIn("СофияUpdate");
        getMvcResultPut(ADMIN, 1, restaurantIn)
                .andExpect(status().isNoContent());
        RestaurantOut restaurantsActual = TestUtil.readFromJsonMvcResult(
                getMvcResultGet(ADMIN, 1).andReturn(), RestaurantOut.class);
        Restaurant restaurant = new Restaurant(1, restaurantIn.getName(), menu1);
        restaurant.setName(restaurantIn.getName());
        assertEquals(new RestaurantOut(restaurant, 0), restaurantsActual);
    }

    @Test
    void updateRestaurantForUnAuthFailed() throws Exception {
        getMvcResultPut(USER_NOT_REGISTRATION, 1, new RestaurantIn("newRestaurant"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteRestaurantWithMenusWithMealsForUserFailed() throws Exception {
        getMvcResultDelete(USER, 2)
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteRestaurantWithMenusWithMealsForAdmin() throws Exception {
        getMvcResultDelete(ADMIN, 2)
                .andExpect(status().isNoContent());
        getMvcResultGet(ADMIN, 2)
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void deleteRestaurantWithMenusWithMealsForUnAuthFailed() throws Exception {
        getMvcResultDelete(USER_NOT_REGISTRATION, 2)
                .andExpect(status().isUnauthorized());
    }
}