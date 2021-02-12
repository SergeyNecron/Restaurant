package ru.study.springboot.web.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import ru.study.springboot.to.RestaurantIn;
import ru.study.springboot.to.RestaurantOut;
import ru.study.springboot.web.TestData;
import ru.study.springboot.web.TestUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.study.springboot.web.TestData.ADMIN;

class AdminRestaurantRestControllerTest extends AbstractRestaurantControllerTest {

    @Test
    void getAllRestaurantsWithMenuNowAdmin() throws Exception {
        getAllRestaurantsWithMenuNow(ADMIN);
    }

    @Test
    void checkGetRestaurantForAdmin() throws Exception {
        MvcResult action = getMvcResultGet(ADMIN, "София")
                .andExpect(status().isOk())
                .andReturn();
        RestaurantOut restaurantsActual = TestUtil.readFromJsonMvcResult(action, RestaurantOut.class);
        RestaurantOut restaurant = new RestaurantOut(TestData.getTestRestaurants().get(0), 0);
        assertEquals(restaurant, restaurantsActual);
    }

    @Test
    void checkCreateRestaurantForAdminOk() throws Exception {
        RestaurantIn restaurantIn = new RestaurantIn("newRestaurant", "ул строителей ..");
        ResultActions action = getMvcResultPost(ADMIN, restaurantIn);
        action.andExpect(status().isCreated());
        RestaurantOut actualRestaurant = TestUtil.readFromJson(action, RestaurantOut.class);
        RestaurantOut restaurantOut = new RestaurantOut(5, restaurantIn.getName(), restaurantIn.getAddress());
        assertEquals(restaurantOut, actualRestaurant);
    }

    @Test
    void checkDuplicateCreateForAdmin() throws Exception {
        RestaurantIn restaurant = new RestaurantIn("София");
        ResultActions action = getMvcResultPost(ADMIN, restaurant);
        action.andExpect(status().isUnprocessableEntity());
    }
}