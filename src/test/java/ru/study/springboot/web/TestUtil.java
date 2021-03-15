package ru.study.springboot.web;

import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import ru.study.springboot.dto.BaseOut;
import ru.study.springboot.dto.RestaurantOut;
import ru.study.springboot.model.User;
import ru.study.springboot.util.JsonUtil;

import java.io.UnsupportedEncodingException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TestUtil {
    public static String getContent(MvcResult result) throws UnsupportedEncodingException {
        return result.getResponse().getContentAsString();
    }

    public static <T> T readFromJson(ResultActions action, Class<T> clazz) throws UnsupportedEncodingException {
        return JsonUtil.readValue(getContent(action.andReturn()), clazz);
    }

    public static <T> T readFromJsonMvcResult(MvcResult result, Class<T> clazz) throws UnsupportedEncodingException {
        return JsonUtil.readValue(getContent(result), clazz);
    }

    public static <T> List<T> readListFromJsonMvcResult(MvcResult result, Class<T> clazz) throws UnsupportedEncodingException {
        return JsonUtil.readValues(getContent(result), clazz);
    }

    public static RequestPostProcessor userHttpBasic(User user) {
        return SecurityMockMvcRequestPostProcessors.httpBasic(user.getEmail(), user.getPassword());
    }

    public static List<RestaurantOut> getTestRestaurantsTo() {
        return TestData.restaurants
                .stream()
                .map(RestaurantOut::new)
                .sorted(Comparator.comparing(BaseOut::getName))
                .collect(Collectors.toList());
    }

}
