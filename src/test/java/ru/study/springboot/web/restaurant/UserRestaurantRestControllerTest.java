package ru.study.springboot.web.restaurant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import ru.study.springboot.error.IllegalRequestDataException;
import ru.study.springboot.model.Role;
import ru.study.springboot.model.User;
import ru.study.springboot.repository.VoteRepository;
import ru.study.springboot.to.RestaurantIn;
import ru.study.springboot.to.RestaurantOut;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.study.springboot.web.restaurant.AdminRestController.REST_URL_RESTAURANT_ADMIN;

class UserRestaurantRestControllerTest extends AbstractRestaurantControllerTest {

    private static final User USER = new User(1, "user@ya.ru", "user", Role.USER);
    private static final User USER_NOT_REGISTRATION = new User(3, "user2@ya.ru", "user2", Role.USER);

    @Autowired
    private VoteRepository votingRepository;

    @Autowired
    private UserRestController userRestController;

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
    void checkReVoteForUserAfterEndTimeException() throws Exception {
        getMvcResultPut(USER, 1);
        LocalDateTime checkTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 0));
        assertThrows(IllegalRequestDataException.class, () -> {
            userRestController.vote(USER, 2, checkTime);
        });

    }

    @Test
    void checkReVoteForUserBeforeEndTime() throws Exception {
        getMvcResultPut(USER, 1);
        LocalDateTime checkTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0));
        RestaurantOut restaurantsActual = userRestController.vote(USER, 2, checkTime).getBody();
        assertEquals(2, restaurantsActual.getId());
    }

    @Test
    void checkCreateRestaurantForUserFailed() throws Exception {
        RestaurantIn restaurant = new RestaurantIn("newRestaurant");
        ResultActions action = getMvcResultPost(USER, REST_URL_RESTAURANT_ADMIN, restaurant);
        action.andExpect(status().isForbidden());
    }

}