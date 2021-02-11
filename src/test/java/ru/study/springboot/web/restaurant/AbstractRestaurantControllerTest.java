package ru.study.springboot.web.restaurant;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;
import ru.study.springboot.model.User;
import ru.study.springboot.to.RestaurantIn;
import ru.study.springboot.to.RestaurantOut;
import ru.study.springboot.to.VoteOut;
import ru.study.springboot.util.JsonUtil;
import ru.study.springboot.web.TestUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.study.springboot.web.TestUtil.getTestRestaurantsTo;
import static ru.study.springboot.web.restaurant.UserRestaurantRestController.REST_URL_RESTAURANT_USER;
import static ru.study.springboot.web.vote.UserVoteRestController.REST_URL_VOTE_USER;

//https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-testing-spring-boot-applications
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
//https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-testing-spring-boot-applications-testing-with-mock-environment
public abstract class AbstractRestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static RequestPostProcessor userHttpBasic(User user) {
        return SecurityMockMvcRequestPostProcessors.httpBasic(user.getEmail(), user.getPassword());
    }

    protected ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {
        return mockMvc.perform(builder);
    }

    protected ResultActions getMvcResultGet(User user, String url) throws Exception {
        return getMvcResult(user, MockMvcRequestBuilders.get(REST_URL_RESTAURANT_USER + "/" + url));
    }

    protected ResultActions getMvcResultPut(User user, Integer id) throws Exception {
        return getMvcResult(user, MockMvcRequestBuilders.put(REST_URL_VOTE_USER + "/" + id));
    }

    protected ResultActions getMvcResultPost(User user, String url, RestaurantIn restaurant) throws Exception {
        return getMvcResult(user, MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(restaurant)));
    }

    protected void checkRestaurant(User user) throws Exception {
        MvcResult action = getMvcResultGet(user, "/")
                .andExpect(status().isOk())
                .andReturn();
        List<RestaurantOut> restaurantsActual = TestUtil.readListFromJsonMvcResult(action, RestaurantOut.class);

        assertEquals(getTestRestaurantsTo(), restaurantsActual);
    }

    protected void checkVoting(User user) throws Exception {
        MvcResult action = getMvcResultPut(user, 1)
                .andExpect(status().isOk())
                .andReturn();
        VoteOut voteOut = TestUtil.readFromJsonMvcResult(action, VoteOut.class);
        assertEquals(1, voteOut.getRestaurantId());
    }

    private ResultActions getMvcResult(User user, MockHttpServletRequestBuilder mockHttpServletRequestBuilder) throws Exception {
        return perform(mockHttpServletRequestBuilder
                .with(userHttpBasic(user)))
                .andDo(print());
    }
}
