package ru.study.springboot.web.profile;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import ru.study.springboot.dto.UserIn;
import ru.study.springboot.dto.UserOut;
import ru.study.springboot.web.TestUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.study.springboot.web.TestData.*;
import static ru.study.springboot.web.profile.AdminProfileRestController.REST_URL_PROFILE_ADMIN;

class AdminProfileRestControllerTest extends AbstractProfileControllerTest {

    @Test
    void getProfileUserFailed() throws Exception {
        getMvcResultGet(USER, 2)
                .andExpect(status().isForbidden());
    }

    @Test
    void getProfileAdmin() throws Exception {
        MvcResult action = getMvcResultGet(ADMIN, 2)
                .andExpect(status().isOk())
                .andReturn();
        UserOut restaurantsActual = TestUtil.readFromJsonMvcResult(action, UserOut.class);
        assertEquals(new UserOut(USER), restaurantsActual);
    }

    @Test
    void getProfileUnAuthFailed() throws Exception {
        getMvcResultGet(USER_NOT_REGISTRATION, 2)
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAllProfileUserFailed() throws Exception {
        getMvcResultGet(USER, REST_URL_PROFILE_ADMIN)
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllProfileAdmin() throws Exception {
        MvcResult action = getMvcResultGet(ADMIN, REST_URL_PROFILE_ADMIN)
                .andExpect(status().isOk())
                .andReturn();
        List<UserOut> restaurantsActual = TestUtil.readListFromJsonMvcResult(action, UserOut.class);
        assertEquals(new UserOut(ADMIN), restaurantsActual.get(0));
        assertEquals(new UserOut(USER), restaurantsActual.get(1));
    }

    @Test
    void getAllProfileUnAuthFailed() throws Exception {
        getMvcResultGet(USER_NOT_REGISTRATION, REST_URL_PROFILE_ADMIN)
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateProfileUserFailed() throws Exception {
        getMvcResultPut(USER, 2, USER_NEW_IN)
                .andExpect(status().isForbidden());
    }

    @Test
    void updateProfileUnAuthFailed() throws Exception {
        getMvcResultPut(USER_NOT_REGISTRATION, 2, USER_NEW_IN)
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateProfileAdmin() throws Exception {
        Integer id = 2; // user update id
        getMvcResultPut(ADMIN, id, USER_NEW_IN).andExpect(status().isNoContent());
        UserOut restaurantsActual = TestUtil.readFromJsonMvcResult(getMvcResultGet(ADMIN, id)
                .andReturn(), UserOut.class);
        assertEquals(new UserOut(USER_NEW_IN, id), restaurantsActual);

        getMvcResultPut(ADMIN, id, new UserIn(USER)).andExpect(status().isNoContent());
    }

    @Test
    void deleteProfileAdmin() throws Exception {
        getMvcResultDelete(ADMIN, 2).andExpect(status().isNoContent());
        getMvcResultGet(ADMIN, 2).andExpect(status().isUnprocessableEntity());
    }

    @Test
    void deleteRepeatProfileAdminFailed() throws Exception {
        getMvcResultDelete(ADMIN, 2).andExpect(status().isNoContent());
        getMvcResultDelete(ADMIN, 2).andExpect(status().isUnprocessableEntity());
    }


    @Test
    void deleteProfileUserFailed() throws Exception {
        getMvcResultDelete(USER, 2)
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteProfileUnAuthFailed() throws Exception {
        getMvcResultDelete(USER_NOT_REGISTRATION, 2).andExpect(status().isUnauthorized());
    }
}