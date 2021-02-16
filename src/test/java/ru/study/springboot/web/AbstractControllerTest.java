package ru.study.springboot.web;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ru.study.springboot.model.AbstractBaseEntity;
import ru.study.springboot.model.User;
import ru.study.springboot.util.JsonUtil;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static ru.study.springboot.web.TestUtil.userHttpBasic;

//https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-testing-spring-boot-applications
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
//https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-testing-spring-boot-applications-testing-with-mock-environment
public abstract class AbstractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    protected ResultActions getMvcResultGet(User user, String url) throws Exception {
        return getMvcResult(user, MockMvcRequestBuilders.get(url));
    }

    protected ResultActions getMvcResultPut(User user, String url) throws Exception {
        return getMvcResult(user, MockMvcRequestBuilders.put(url));
    }

    protected ResultActions getMvcResultPost(User user, String url, AbstractBaseEntity entity) throws Exception {
        return getMvcResult(user, MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(entity)));
    }

    protected ResultActions getMvcResultPut(User user, String url, AbstractBaseEntity entity) throws Exception {
        return getMvcResult(user, MockMvcRequestBuilders.put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(entity)));
    }

    protected ResultActions getMvcResultDelete(User user, String url) throws Exception {
        return getMvcResult(user, MockMvcRequestBuilders.delete(url));
    }

    private ResultActions getMvcResult(User user, MockHttpServletRequestBuilder mockHttpServletRequestBuilder) throws Exception {
        return perform(mockHttpServletRequestBuilder
                .with(userHttpBasic(user)))
                .andDo(print());
    }

    private ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {
        return mockMvc.perform(builder);
    }
}