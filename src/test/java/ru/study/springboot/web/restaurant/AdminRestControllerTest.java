package ru.study.springboot.web.restaurant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import ru.study.springboot.model.Role;
import ru.study.springboot.model.User;
import ru.study.springboot.repository.VoteRepository;
import ru.study.springboot.to.RestaurantIn;
import ru.study.springboot.to.RestaurantOut;
import ru.study.springboot.web.TestData;
import ru.study.springboot.web.TestUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.study.springboot.web.restaurant.AdminRestController.REST_URL_RESTAURANT_ADMIN;

class AdminRestControllerTest extends AbstractControllerTest {

    private static final User ADMIN = new User(2, "admin@ya.ru", "admin", Role.USER, Role.ADMIN);

    @Autowired
    private VoteRepository votingRepository;

    @BeforeEach
    public void clear() {
        votingRepository.deleteAll();
    }

    @Test
    void checkRestaurantForAdmin() throws Exception {
        checkRestaurant(ADMIN);
    }

    @Test
    void checkVotingForAdmin() throws Exception {
        checkVoting(ADMIN);
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
        ResultActions action = getMvcResultPost(ADMIN, REST_URL_RESTAURANT_ADMIN, restaurantIn);
        action.andExpect(status().isCreated());
        RestaurantOut actualRestaurant = TestUtil.readFromJson(action, RestaurantOut.class);
        RestaurantOut restaurantOut = new RestaurantOut(5, restaurantIn.getName(), restaurantIn.getAddress());
        assertEquals(restaurantOut, actualRestaurant);
    }

    @Test
    void checkDuplicateCreateForAdmin() throws Exception {
        RestaurantIn restaurant = new RestaurantIn("София");
        ResultActions action = getMvcResultPost(ADMIN, REST_URL_RESTAURANT_ADMIN, restaurant);
        action.andExpect(status().isUnprocessableEntity());
    }

//    @Test
//    void checkMaxCountRestaurantCreateForUser() throws Exception {
//        RestaurantOut restaurant = new RestaurantOut("newRestaurant");
//        Integer countRestaurant = restaurantRepository.countByDate(LocalDate.now());
//        ResultActions action = getMvcResultPost(ADMIN, restaurant);
//        for (int i = countRestaurant; i <= MAX_COUNT_MEALS_FOR_MENU; i++) {
//            new RestaurantOut("newRestaurant" + i);
//            action = getMvcResultPost(ADMIN, restaurant);
//        }
//        action.andExpect(status().isUnprocessableEntity());
//    }
}