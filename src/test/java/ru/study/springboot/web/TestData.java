package ru.study.springboot.web;

import ru.study.springboot.dto.MenuIn;
import ru.study.springboot.dto.UserIn;
import ru.study.springboot.model.*;

import java.time.LocalDate;
import java.util.List;

public class TestData {

    public static final User ADMIN = new User(1, "admin", "admin@ya.ru", "admin", Role.USER, Role.ADMIN);
    public static final User USER = new User(2, "user", "user@ya.ru", "user", Role.USER);
    public static final User USER_NOT_REGISTRATION = new User(3, "user", "user2@ya.ru", "user2", Role.USER);
    public static final UserIn USER_NEW_IN = new UserIn("newUser", "newuser1@ya.ru", "password1");
    public static final MenuIn NEW_MENU_IN = new MenuIn("newMenu", LocalDate.now(), 1,
            List.of(new Meal("рагу", 300.0), new Meal("жареная индейка", 800.0)));
    public static final MenuIn NEW_MENU_IN_MIN_VALID = new MenuIn("newMenu", LocalDate.now(), 1,
            List.of(new Meal("рагу", 300.0)));
    public static final MenuIn NEW_MENU_IN_MAX_VALID = new MenuIn("newMenu", LocalDate.now(), 1, List.of(
            new Meal("рагу", 300.0), new Meal("жареная индейка", 800.0),
            new Meal("рагу", 300.0), new Meal("жареная индейка", 800.0),
            new Meal("рагу", 300.0), new Meal("жареная индейка", 800.0),
            new Meal("рагу", 300.0), new Meal("жареная индейка", 800.0)));

    public static Menu menu1;
    public static Menu menu2;
    public static Menu menu3;
    public static Menu menu4;
    public static Restaurant restaurant1;
    public static Restaurant restaurant2;
    public static Restaurant restaurant3;
    public static Restaurant restaurant4;

    static {
        final List<Meal> meals1 = List.of(
                new Meal("Бизнес-ланч", 100.0),
                new Meal("Тёплые ролы", 99.0),
                new Meal("Салат Цезарь", 99.0),
                new Meal("Лимонад", 90.0),
                new Meal("Пицца", 58.9),
                new Meal("Мороженное", 8.0));

        final List<Meal> meals2 = List.of(
                new Meal("Суп", 50.0),
                new Meal("Отбивная", 20.0),
                new Meal("Картофельное пюре", 80.0),
                new Meal("Амлет", 70.0),
                new Meal("Хачапури", 50.0),
                new Meal("Чай", 50.0));

        final List<Meal> meals3 = List.of(
                new Meal("Ланч", 200.0),
                new Meal("Ролы", 99.0),
                new Meal("Салат фруктовый", 99.0),
                new Meal("Кофе", 60.0),
                new Meal("Бургер", 70.0),
                new Meal("Гамбургер", 60.0));

        final List<Meal> meals4 = List.of(
                new Meal("Салат Цезарь", 80.0),
                new Meal("Спагетти с соусом", 50.0),
                new Meal("Щи", 40.0),
                new Meal("Ножки индейки", 500.0),
                new Meal("Горячий шоколад", 80.0),
                new Meal("Кофе", 50.5));

        menu1 = new Menu("Меню дня", meals1);
        menu2 = new Menu("Меню дня", meals2);
        menu3 = new Menu("Меню дня", meals3);
        menu4 = new Menu("Меню дня", meals4);

        restaurant1 = new Restaurant(1, "София", menu1);
        restaurant2 = new Restaurant(2, "Макдак", menu2);
        restaurant3 = new Restaurant(3, "Пицерия", menu3);
        restaurant4 = new Restaurant(4, "Закусочная", menu4);

        menu1.setRestaurant(restaurant1);
        menu2.setRestaurant(restaurant2);
        menu3.setRestaurant(restaurant3);
        menu4.setRestaurant(restaurant4);
    }

    public static List<Menu> menus = List.of(menu1, menu2, menu3, menu4);
    public static List<Restaurant> restaurants = List.of(restaurant1, restaurant2, restaurant3, restaurant4);
}
