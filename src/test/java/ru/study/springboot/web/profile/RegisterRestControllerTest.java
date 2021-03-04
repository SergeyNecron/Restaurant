package ru.study.springboot.web.profile;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import ru.study.springboot.dto.UserOut;
import ru.study.springboot.model.Role;
import ru.study.springboot.web.TestUtil;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.study.springboot.web.TestData.USER_NEW_IN;

class RegisterRestControllerTest extends AbstractProfileControllerTest {

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