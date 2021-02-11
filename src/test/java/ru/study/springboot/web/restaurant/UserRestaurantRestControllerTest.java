package ru.study.springboot.web.restaurant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import ru.study.springboot.model.Role;
import ru.study.springboot.model.User;
import ru.study.springboot.repository.VoteRepository;
import ru.study.springboot.to.RestaurantIn;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.study.springboot.web.TestData.USER;
import static ru.study.springboot.web.restaurant.AdminRestaurantRestController.REST_URL_RESTAURANT_ADMIN;

class UserRestaurantRestControllerTest extends AbstractRestaurantControllerTest {

    private static final User USER_NOT_REGISTRATION = new User(3, "user2@ya.ru", "user2", Role.USER);

    @Autowired
    private VoteRepository votingRepository;

    @BeforeEach
    public void clear() {
        votingRepository.deleteAll();
    }

    @Test
    void checkRestaurantForUser() throws Exception {
        checkRestaurant(USER);
    }

    @Test
    void checkUnauthorizedGet() throws Exception {
        getMvcResultGet(USER_NOT_REGISTRATION, "/")
                .andExpect(status().isUnauthorized());
    }

    @Test
    void checkVotingForUser() throws Exception {
        checkVoting(USER);
    }

    @Test
    void checkUnauthorizedVoting() throws Exception {
        getMvcResultGet(USER_NOT_REGISTRATION, "/2")
                .andExpect(status().isUnauthorized());
    }

    @Test
    void checkUnauthorizedCreate() throws Exception {
        getMvcResultPost(USER_NOT_REGISTRATION, REST_URL_RESTAURANT_ADMIN, new RestaurantIn("newRestaurant"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void checkCreateRestaurantForUserFailed() throws Exception {
        RestaurantIn restaurant = new RestaurantIn("newRestaurant");
        ResultActions action = getMvcResultPost(USER, REST_URL_RESTAURANT_ADMIN, restaurant);
        action.andExpect(status().isForbidden());
    }
}