package ru.study.springboot.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import ru.study.springboot.TestData;
import ru.study.springboot.model.Menu;
import ru.study.springboot.model.Role;
import ru.study.springboot.model.User;
import ru.study.springboot.repository.MenuRepository;
import ru.study.springboot.repository.UserRepository;
import ru.study.springboot.to.MenuRating;
import ru.study.springboot.util.JsonUtil;
import ru.study.springboot.util.MenuUtil;
import ru.study.springboot.util.ValidationUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.study.springboot.util.MenuUtil.isBetweenHalfOpen;
import static ru.study.springboot.util.ValidationUtil.checkNotFoundWithName;
import static ru.study.springboot.web.MenuRestController.*;

//https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-testing-spring-boot-applications
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@SpringBootTest
@AutoConfigureMockMvc
class MenuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private UserRepository userRepository;

    protected ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {
        return mockMvc.perform(builder);
    }

    public static final User user = new User(1, "user@ya.ru", "user", Role.USER);
    public static final User admin = new User(2, "admin@ya.ru", "admin", Role.USER, Role.ADMIN);

    @BeforeEach
    public void clear() {
        menuRepository.deleteAll();
        menuRepository.saveAll(TestData.getTestMenus());
    }

    @Test
    void getUnauthorizedGet() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_MENU + GET_MENU))
                .andExpect(status().isUnauthorized());
    }
    @Test
    void getUnauthorizedVoting() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_MENU + "/2"))
                .andExpect(status().isUnauthorized());
    }
    @Test
    void getUnauthorizedCrete() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_MENU + CREATE))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void checkMenuForUser() throws Exception {
        checkMenu(user);
    }
    @Test
    void checkMenuForAdmin() throws Exception {
        checkMenu(admin);
    }
    private void checkMenu(User user) throws Exception {
        MvcResult action = getMvcResult(user, GET_MENU);
        List<MenuRating> menusActual = TestUtil.readListFromJsonMvcResult(action, MenuRating.class);
        List<MenuRating> menus = TestData.getTestMenusTo();
        assertEquals(menus, menusActual);
    }
    private MvcResult getMvcResult(User user, String url) throws Exception {
        return  perform(MockMvcRequestBuilders.get(REST_URL_MENU + url)
                .with(userHttpBasic(user)))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();
    }
    @Test
    void checkReVoteForUser() throws Exception {
        String saloon = "Пицерия";
        getMvcResultPut(user, saloon);
        ResultActions action = getMvcResultPut(user, saloon);
        LocalTime checkTime = LocalTime.now();
        if(isBetweenHalfOpen(checkTime, startTime, endTime))
            action.andExpect(status().isOk());
        else action.andExpect(status().isUnprocessableEntity());
        User oldUser = checkNotFoundWithName(userRepository.getByEmail(user.getEmail()), user.getEmail());
        oldUser.setVail(null);
        userRepository.save(oldUser);
    }
    @Test
    void checkVotingForUser() throws Exception {
        checkVoting(user);
    }
    @Test
    void checkVotingForAdmin() throws Exception {
        checkVoting(admin);
    }
    private void checkVoting(User user) throws Exception {
        String saloon = "София";
        ResultActions action = getMvcResultPut(user, saloon );
        action.andExpect(status().isOk());
        User updateUser = TestUtil.readFromJson(action, User.class);
        Menu menu = checkNotFoundWithName(menuRepository.getBySaloon(saloon),saloon);
        user.setVail(menu.id());
        assertEquals(user, updateUser);
    }
    private ResultActions getMvcResultPut(User user, String url) throws Exception {
        return  perform(MockMvcRequestBuilders.put(REST_URL_MENU + url)
                .with(userHttpBasic(user)))
                .andDo(print());
    }

    @Test
    void checkCreateMenuForUserFailed() throws Exception {
        Menu menu = TestData.getTestMenus().get(0);
        menu.setSaloon("newMenu");
        ResultActions action = getMvcResultPost(user, menu, CREATE);
        action.andExpect(status().isForbidden());
    }
    @Test
    void checkCreateMenuForAdminOk() throws Exception {
        Menu menu = TestData.getTestMenus().get(0);
        menu.setSaloon("newMenu");
        ResultActions action = getMvcResultPost(admin, menu, CREATE);
        action.andExpect(status().isCreated());
        MenuRating actualMenu = TestUtil.readFromJson(action, MenuRating.class);
        assertEquals(MenuUtil.to(menu,0), actualMenu);
    }
    @Test
    void checkMaxCountMenuCreateForUser() throws Exception {
        Menu menu = TestData.getTestMenus().get(0);
        menu.setSaloon("newMenu");
        Integer countMenu = menuRepository.countByDate(LocalDate.now());
        ResultActions action = getMvcResultPost(admin, menu, CREATE);
        for (int i = countMenu; i <= MAX_COUNT_MENU; i++) {
            menu.setSaloon("newMenu"+i);
            action = getMvcResultPost(admin, menu, CREATE);
        }
        action.andExpect(status().isUnprocessableEntity());
    }
    @Test
    void checkDuplicateCreateForUser() throws Exception {
        Menu menu = TestData.getTestMenus().get(0);
        menu.setSaloon("София");
        ResultActions action =  getMvcResultPost(admin, menu, CREATE);
        action.andExpect(status().isUnprocessableEntity());
    }
    private ResultActions getMvcResultPost(User user,  Menu menu, String url) throws Exception {
        return  perform(MockMvcRequestBuilders.post(REST_URL_MENU + url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(menu))
                .with(userHttpBasic(user)))
                .andDo(print());
    }

    public static RequestPostProcessor userHttpBasic(User user) {
        return SecurityMockMvcRequestPostProcessors.httpBasic(user.getEmail(), user.getPassword());
    }
}