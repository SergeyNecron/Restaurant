package ru.study.springboot.web.menu;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import ru.study.springboot.dto.MenuIn;
import ru.study.springboot.dto.MenuOut;
import ru.study.springboot.web.AbstractControllerTest;
import ru.study.springboot.web.TestUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.study.springboot.web.TestData.*;
import static ru.study.springboot.web.menu.AdminMenuRestController.REST_URL_MENU_ADMIN;

class AdminMenuRestControllerTest extends AbstractControllerTest {

    @Test
    void getMenuWithMealsForAdmin() throws Exception {
        MvcResult action = getMvcResultGet(ADMIN, REST_URL_MENU_ADMIN + "/" + 1)
                .andExpect(status().isOk())
                .andReturn();
        MenuOut menusActual = TestUtil.readFromJsonMvcResult(action, MenuOut.class);
        assertEquals(new MenuOut(menu1), menusActual);
    }

    @Test
    void getAllMenusWithMealsForAdmin() throws Exception {
        MvcResult action = getMvcResultGet(ADMIN, REST_URL_MENU_ADMIN)
                .andExpect(status().isOk())
                .andReturn();
        List<MenuOut> menusActual = TestUtil.readListFromJsonMvcResult(action, MenuOut.class);
        assertEquals(menus.stream()
                .map(MenuOut::new)
                .collect(Collectors.toList()), menusActual);
    }

    @Test
    void getAllMenusWithMealsByDateForAdmin() throws Exception {
        MvcResult action = getMvcResultGet(ADMIN, REST_URL_MENU_ADMIN + "/date/" + LocalDate.now())
                .andExpect(status().isOk())
                .andReturn();
        List<MenuOut> menusActual = TestUtil.readListFromJsonMvcResult(action, MenuOut.class);
        assertEquals(menus.stream()
                .filter(it -> it.getDate().equals(LocalDate.now()))
                .map(MenuOut::new)
                .collect(Collectors.toList()), menusActual);
    }

    @Test
    void createMenuWithMealsForAdmin() throws Exception {
        MvcResult action = getMvcResultPost(ADMIN, REST_URL_MENU_ADMIN, NEW_MENU_IN)
                .andExpect(status().isCreated())
                .andReturn();
        MenuOut menusActual = TestUtil.readFromJsonMvcResult(action, MenuOut.class);
        MenuOut expected = new MenuOut(NEW_MENU_IN, 5);
        assertEquals(expected, menusActual);
    }

    @Test
    void createDuplicateMenuWithMealsForAdminFailed() throws Exception {
        getMvcResultPost(ADMIN, REST_URL_MENU_ADMIN, NEW_MENU_IN);
        getMvcResultPost(ADMIN, REST_URL_MENU_ADMIN, NEW_MENU_IN)
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateMenuWithMealsForAdmin() throws Exception {
        getMvcResultPut(ADMIN, REST_URL_MENU_ADMIN + "/" + 1, NEW_MENU_IN)
                .andExpect(status().isNoContent());
        MenuOut menusActual = TestUtil.readFromJsonMvcResult(getMvcResultGet(ADMIN, REST_URL_MENU_ADMIN + "/" + 1)
                .andReturn(), MenuOut.class);
        assertEquals(new MenuOut(NEW_MENU_IN, 1), menusActual);
    }

    @Test
    void deleteMenuWithMealsForAdmin() throws Exception {
        getMvcResultDelete(ADMIN, REST_URL_MENU_ADMIN + "/" + 1)
                .andExpect(status().isNoContent());
        getMvcResultGet(ADMIN, REST_URL_MENU_ADMIN + "/" + 1)
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void deleteRepeatMenuWithMealsForAdminFailed() throws Exception {
        getMvcResultDelete(ADMIN, REST_URL_MENU_ADMIN + "/" + 1)
                .andExpect(status().isNoContent());
        getMvcResultDelete(ADMIN, REST_URL_MENU_ADMIN + "/" + 1)
                .andExpect(status().isUnprocessableEntity());

    }

    @Test
    void getMenuWithMealsForUserFailed() throws Exception {
        getMvcResultGet(USER, REST_URL_MENU_ADMIN + "/" + 1)
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllMenusWithMealsForUserFailed() throws Exception {
        getMvcResultGet(USER, REST_URL_MENU_ADMIN)
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllMenusWithMealsByDateForUserFailed() throws Exception {
        getMvcResultGet(USER, REST_URL_MENU_ADMIN + "/date/" + LocalDate.now())
                .andExpect(status().isForbidden());
    }

    @Test
    void createMenuWithMealsForUserFailed() throws Exception {
        getMvcResultPost(USER, REST_URL_MENU_ADMIN, NEW_MENU_IN)
                .andExpect(status().isForbidden());
    }

    @Test
    void updateMenuWithMealsForUserFailed() throws Exception {
        getMvcResultPut(USER, REST_URL_MENU_ADMIN + "/" + 1, NEW_MENU_IN)
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteMenuWithMealsForUserFailed() throws Exception {
        getMvcResultDelete(USER, REST_URL_MENU_ADMIN + "/" + 1)
                .andExpect(status().isForbidden());
    }

    @Test
    void getMenuWithMealsForUnAuthFailed() throws Exception {
        getMvcResultGet(USER_NOT_REGISTRATION, REST_URL_MENU_ADMIN + "/" + 1)
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAllMenusWithMealsForUnAuthFailed() throws Exception {
        getMvcResultGet(USER_NOT_REGISTRATION, REST_URL_MENU_ADMIN)
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAllMenusWithMealsByDateForUnAuthFailed() throws Exception {
        getMvcResultGet(USER_NOT_REGISTRATION, REST_URL_MENU_ADMIN + "/date/" + LocalDate.now())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createMenuWithMealsForUnAuthFailed() throws Exception {
        getMvcResultPost(USER_NOT_REGISTRATION, REST_URL_MENU_ADMIN, NEW_MENU_IN)
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateMenuWithMealsForUnAuthFailed() throws Exception {
        getMvcResultPut(USER_NOT_REGISTRATION, REST_URL_MENU_ADMIN + "/" + 1, NEW_MENU_IN)
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteMenuWithMealsForUnAuthFailed() throws Exception {
        getMvcResultDelete(USER_NOT_REGISTRATION, REST_URL_MENU_ADMIN + "/" + 1)
                .andExpect(status().isUnauthorized());
    }

    @Test
    void checkMNCountMealsCreateForAdminFailed() throws Exception {
        getMvcResultPost(ADMIN, REST_URL_MENU_ADMIN, NEW_MENU_IN_MIN_VALID)
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void checkMaxCountMealsCreateForAdminFailed() throws Exception {
        getMvcResultPost(ADMIN, REST_URL_MENU_ADMIN, NEW_MENU_IN_MAX_VALID)
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void checkNotFoundWithIdRestaurantFailed() throws Exception {
        MenuIn menuIn = new MenuIn(NEW_MENU_IN.getName(), NEW_MENU_IN.getDate(), 20, NEW_MENU_IN.getMeals());
        getMvcResultPut(ADMIN, REST_URL_MENU_ADMIN + "/" + 2, menuIn)
                .andExpect(status().isUnprocessableEntity());
    }
}