package ru.study.springboot.web.menu;

import org.junit.jupiter.api.Test;
import ru.study.springboot.web.AbstractControllerTest;

class AdminMenuRestControllerTest extends AbstractControllerTest {

    @Test
    void getMenuWithMealsForAdmin() {
    }

    @Test
    void getAllMenusWithMealsForAdmin() {
    }

    @Test
    void getAllMenusWithMealsByDateForAdmin() {
    }

    @Test
    void createMenuWithMealsForAdmin() {
    }

    @Test
    void deleteMenuWithMealsForAdmin() {
    }

    @Test
    void updateMenuWithMealsForAdmin() {
    }


    //    @Test
//    void checkMaxCountRestaurantCreateForUser() throws Exception {
//        RestaurantOut restaurant = new RestaurantOut("newRestaurant");
//        Integer countRestaurant = restaurantRepository.countByDate(LocalDate.now());
//        ResultActions action = getMvcResultPost(ADMIN, restaurant);
//        for (int i = countRestaurant; i <= MAX_COUNT_MEALS_FOR_MENU; i++) {
//            new RestaurantOut("newRestaurant" + i);
//            action = getMvcResultPost(ADMIN, restaurant);
//        }
//        action.andExpect(status().isUnprocessableEntity());
//    }
}