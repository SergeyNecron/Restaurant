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
import ru.study.springboot.StartData;
import ru.study.springboot.model.Menu;
import ru.study.springboot.model.Role;
import ru.study.springboot.model.User;
import ru.study.springboot.repository.MenuRepository;
import ru.study.springboot.repository.VoteRepository;
import ru.study.springboot.to.MenuTo;
import ru.study.springboot.to.VoteTo;
import ru.study.springboot.util.JsonUtil;
import ru.study.springboot.util.MenuUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.study.springboot.util.DateTimeUtil.isBetweenHalfOpen;
import static ru.study.springboot.util.ValidationUtil.checkNotFoundWithName;
import static ru.study.springboot.web.MenuRestController.*;

//https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-testing-spring-boot-applications
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@SpringBootTest
@AutoConfigureMockMvc
class MenuControllerTest {
    private static final User user = new User(1, "user@ya.ru", "user", Role.USER);
    private static final User admin = new User(2, "admin@ya.ru", "admin", Role.USER, Role.ADMIN);
    private static final User userNotRegistration = new User(3, "user2@ya.ru", "user2", Role.USER);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private VoteRepository votingRepository;

    public static List<MenuTo> getTestMenusTo() {
        return StartData.getTestMenus()
                .stream()
                .map(it -> MenuUtil.to(it, 0))
                .collect(Collectors.toList());
    }

    private static MenuTo getMenuTo(String name) {
        Menu menu = StartData.getTestMenus().get(0);
        return new MenuTo(name, menu.getMeals(), 0);
    }

    private static RequestPostProcessor userHttpBasic(User user) {
        return SecurityMockMvcRequestPostProcessors.httpBasic(user.getEmail(), user.getPassword());
    }

    @BeforeEach
    public void clear() {
        menuRepository.deleteAll();
        votingRepository.deleteAll();
        menuRepository.saveAll(StartData.getTestMenus());
    }

    @Test
    void checkMenuForUser() throws Exception {
        checkMenu(user);
    }

    @Test
    void checkMenuForAdmin() throws Exception {
        checkMenu(admin);
    }

    @Test
    void checkUnauthorizedGet() throws Exception {
        getMvcResultForRequestGet(userNotRegistration, GET_MENU)
                .andExpect(status().isUnauthorized());
    }

    @Test
    void checkVotingForUser() throws Exception {
        checkVoting(user);
    }

    @Test
    void checkVotingForAdmin() throws Exception {
        checkVoting(admin);
    }

    @Test
    void checkUnauthorizedVoting() throws Exception {
        getMvcResultForRequestGet(userNotRegistration, "/2")
                .andExpect(status().isUnauthorized());
    }

    @Test
    void checkUnauthorizedCrete() throws Exception {
        getMvcResultForRequestGet(userNotRegistration, CREATE)
                .andExpect(status().isUnauthorized());
    }

    @Test
    void checkReVoteForUser() throws Exception {
        String saloon = "Пицерия";
        getMvcResultForRequestPut(user, saloon);
        ResultActions action = getMvcResultForRequestPut(user, saloon);
        LocalTime checkTime = LocalTime.now();
        if (isBetweenHalfOpen(checkTime, startTime, endTime)) {
            action.andExpect(status().isOk());
            equalsVoting(saloon, action, user);
        } else action.andExpect(status().isUnprocessableEntity());
    }

    @Test
    void checkCreateMenuForUserFailed() throws Exception {
        MenuTo menu = getMenuTo("newMenu");
        ResultActions action = getMvcResultPost(user, menu, CREATE);
        action.andExpect(status().isForbidden());
    }

    @Test
    void checkCreateMenuForAdminOk() throws Exception {
        MenuTo menu = getMenuTo("newMenu");
        ResultActions action = getMvcResultPost(admin, menu, CREATE);
        action.andExpect(status().isCreated());
        MenuTo actualMenu = TestUtil.readFromJson(action, MenuTo.class);
        assertEquals(menu, actualMenu);
    }

    @Test
    void checkMaxCountMenuCreateForUser() throws Exception {
        MenuTo menu = getMenuTo("newMenu");
        Integer countMenu = menuRepository.countByDate(LocalDate.now());
        ResultActions action = getMvcResultPost(admin, menu, CREATE);
        for (int i = countMenu; i <= MAX_COUNT_MENU; i++) {
            getMenuTo("newMenu" + i);
            action = getMvcResultPost(admin, menu, CREATE);
        }
        action.andExpect(status().isUnprocessableEntity());
    }

    @Test
    void checkDuplicateCreateForUser() throws Exception {
        MenuTo menu = getMenuTo("София");
        ResultActions action = getMvcResultPost(admin, menu, CREATE);
        action.andExpect(status().isUnprocessableEntity());
    }

    private void equalsVoting(String saloon, ResultActions action, User user) throws java.io.UnsupportedEncodingException {
        VoteTo newVoting = TestUtil.readFromJson(action, VoteTo.class);
        Menu menu = checkNotFoundWithName(menuRepository.getBySaloon(saloon), saloon);
        assertEquals(new VoteTo(LocalDate.now(), user.getId(), menu.getSaloon()), newVoting);
    }

    private void checkMenu(User user) throws Exception {
        MvcResult action = getMvcResultForRequestGet(user, GET_MENU)
                .andExpect(status().isOk())
                .andReturn();
        List<MenuTo> menusActual = TestUtil.readListFromJsonMvcResult(action, MenuTo.class);
        assertEquals(getTestMenusTo(), menusActual);
    }

    private void checkVoting(User user) throws Exception {
        String saloon = "София";
        ResultActions action = getMvcResultForRequestPut(user, saloon);
        action.andExpect(status().isOk());
        equalsVoting(saloon, action, user);
    }

    private ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {
        return mockMvc.perform(builder);
    }

    private ResultActions getMvcResultForRequestGet(User user, String url) throws Exception {
        return getMvcResult(user, GET_MENU, MockMvcRequestBuilders.get(REST_URL_MENU + url));
    }

    private ResultActions getMvcResultForRequestPut(User user, String url) throws Exception {
        return getMvcResult(user, GET_MENU, MockMvcRequestBuilders.put(REST_URL_MENU + url));
    }

    private ResultActions getMvcResultPost(User user, MenuTo menu, String url) throws Exception {
        return getMvcResult(user, GET_MENU, MockMvcRequestBuilders.post(REST_URL_MENU + url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(menu)));
    }

    private ResultActions getMvcResult(User user, String url, MockHttpServletRequestBuilder mockHttpServletRequestBuilder) throws Exception {
        return perform(mockHttpServletRequestBuilder
                .with(userHttpBasic(user)))
                .andDo(print());
    }
}