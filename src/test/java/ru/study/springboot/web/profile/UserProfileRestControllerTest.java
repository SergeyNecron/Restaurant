package ru.study.springboot.web.profile;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import ru.study.springboot.dto.UserIn;
import ru.study.springboot.dto.UserOut;
import ru.study.springboot.model.Role;
import ru.study.springboot.model.User;
import ru.study.springboot.web.TestUtil;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.study.springboot.web.TestData.*;
import static ru.study.springboot.web.profile.UserProfileRestController.REST_URL_PROFILE_USER;

class UserProfileRestControllerTest extends AbstractProfileControllerTest {

    @Test
    void getProfileUser() throws Exception {
        get(USER);
    }

    @Test
    void getProfileAdmin() throws Exception {
        get(ADMIN);
    }

    @Test
    void getProfileUnAuthFailed() throws Exception {
        getMvcResultGet(USER_NOT_REGISTRATION, REST_URL_PROFILE_USER)
                .andExpect(status().isUnauthorized());
    }

    private void get(User user) throws Exception {
        MvcResult action = getMvcResultGet(user, REST_URL_PROFILE_USER)
                .andExpect(status().isOk())
                .andReturn();
        UserOut restaurantsActual = TestUtil.readFromJsonMvcResult(action, UserOut.class);
        assertEquals(new UserOut(user), restaurantsActual);
    }

    @Test
    void updateProfileUser() throws Exception {
        update(USER, USER_NEW_IN);
    }

    @Test
    void updateProfileAdmin() throws Exception {
        update(ADMIN, USER_NEW_IN);
    }

    @Test
    void updateProfileUnAuthFailed() throws Exception {
        getMvcResultPut(USER_NOT_REGISTRATION, USER_NEW_IN)
                .andExpect(status().isUnauthorized());
    }

    private void update(User user, UserIn userIn) throws Exception {
        getMvcResultPut(user, userIn).andExpect(status().isNoContent());
        UserOut restaurantsActual = TestUtil.readFromJsonMvcResult(getMvcResultGet(
                new User(userIn.getName(), userIn.getEmail(), userIn.getPassword(), userIn.getRoles()), REST_URL_PROFILE_USER)
                .andReturn(), UserOut.class);
        assertEquals(new UserOut(userIn, user.id()), restaurantsActual);

        getMvcResultPut(new User(userIn.getName(), userIn.getEmail(), userIn.getPassword(), userIn.getRoles()),
                new UserIn(user)).andExpect(status().isNoContent());
    }

    @Test
    void deleteProfileUser() throws Exception {
        getMvcResultDelete(USER).andExpect(status().isNoContent());
        getMvcResultGet(USER, REST_URL_PROFILE_USER).andExpect(status().isUnauthorized());
    }

    @Test
    void deleteProfileAdminFailed() throws Exception {
        getMvcResultDelete(ADMIN).andExpect(status().isUnprocessableEntity());
        getMvcResultGet(ADMIN, REST_URL_PROFILE_USER).andExpect(status().isOk());
    }

    @Test
    void deleteProfileUnAuthFailed() throws Exception {
        getMvcResultDelete(USER_NOT_REGISTRATION).andExpect(status().isUnauthorized());
    }

    @Test
    void register() throws Exception {
        ResultActions action = getMvcResultPost(USER_NEW_IN);
        action.andExpect(status().isCreated());
        UserOut actualProfile = TestUtil.readFromJson(action, UserOut.class);
        UserOut userOut = new UserOut(USER_NEW_IN, 3);
        userOut.setRoles(EnumSet.of(Role.USER));
        assertEquals(userOut, actualProfile);
    }
}