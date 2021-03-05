package ru.study.springboot.web.profile;

import org.springframework.test.web.servlet.ResultActions;
import ru.study.springboot.dto.UserIn;
import ru.study.springboot.model.User;
import ru.study.springboot.web.AbstractControllerTest;

import static ru.study.springboot.web.profile.AdminProfileRestController.REST_URL_PROFILE_ADMIN;
import static ru.study.springboot.web.profile.UserProfileRestController.REST_URL_PROFILE_USER;

public abstract class AbstractProfileControllerTest extends AbstractControllerTest {

    protected ResultActions getMvcResultGet(User user, Integer id) throws Exception {
        return super.getMvcResultGet(user, REST_URL_PROFILE_ADMIN + "/" + id);
    }

    protected ResultActions getMvcResultPost(UserIn userIn) throws Exception {
        return super.getMvcResultPost(REST_URL_PROFILE_USER + "/register", userIn);
    }

    protected ResultActions getMvcResultPut(User user, Integer id, UserIn userIn) throws Exception {
        return super.getMvcResultPut(user, REST_URL_PROFILE_ADMIN + "/" + id, userIn);
    }

    protected ResultActions getMvcResultDelete(User user, Integer id) throws Exception {
        return super.getMvcResultDelete(user, REST_URL_PROFILE_ADMIN + "/" + id);
    }

    protected ResultActions getMvcResultPut(User user, UserIn userIn) throws Exception {
        return super.getMvcResultPut(user, REST_URL_PROFILE_USER, userIn);
    }

    protected ResultActions getMvcResultDelete(User user) throws Exception {
        return super.getMvcResultDelete(user, REST_URL_PROFILE_USER);
    }
}