package ru.study.springboot.web;

import ru.study.springboot.model.*;
import ru.study.springboot.to.MenuIn;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TestData {

    public static final User ADMIN = new User(2, "admin@ya.ru", "admin", Role.USER, Role.ADMIN);
    public static final User USER = new User(1, "user@ya.ru", "user", Role.USER);
    public static final User USER_NOT_REGISTRATION = new User(3, "user2@ya.ru", "user2", Role.USER);
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
    public static List<Menu> menus = new ArrayList<>();
    public static Restaurant restaurant1;

    static Restaurant restaurant2;
    static Restaurant restaurant3;
    static Restaurant restaurant4;

    static {
        final Menu menu2;
        final Menu menu3;
        final Menu menu4;

        final List<Meal> meals1 = new ArrayList();
        final List<Meal> meals2 = new ArrayList();
        final List<Meal> meals3 = new ArrayList();
        final List<Meal> meals4 = new ArrayList();

        meals1.add(new Meal("Бизнес-ланч", 100.0));
        meals1.add(new Meal("Тёплые ролы", 99.0));
        meals1.add(new Meal("Салат Цезарь", 99.0));
        meals1.add(new Meal("Лимонад", 90.0));
        meals1.add(new Meal("Пицца", 58.9));
        meals1.add(new Meal("Мороженное", 8.0));

        meals2.add(new Meal("Суп", 50.0));
        meals2.add(new Meal("Отбивная", 20.0));
        meals2.add(new Meal("Картофельное пюре", 80.0));
        meals2.add(new Meal("Амлет", 70.0));
        meals2.add(new Meal("Хачапури", 50.0));
        meals2.add(new Meal("Чай", 50.0));

        meals3.add(new Meal("Ланч", 200.0));
        meals3.add(new Meal("Ролы", 99.0));
        meals3.add(new Meal("Салат фруктовый", 99.0));
        meals3.add(new Meal("Кофе", 60.0));
        meals3.add(new Meal("Бургер", 70.0));
        meals3.add(new Meal("Гамбургер", 60.0));

        meals4.add(new Meal("Салат Цезарь", 80.0));
        meals4.add(new Meal("Спагетти с соусом", 50.0));
        meals4.add(new Meal("Щи", 40.0));
        meals4.add(new Meal("Ножки индейки", 500.0));
        meals4.add(new Meal("Горячий шоколад", 80.0));
        meals4.add(new Meal("Кофе", 50.5));

        menu1 = new Menu("Меню дня", meals1);
        menu1.setId(1);
        restaurant1 = new Restaurant(1, "София", menu1);
        menu1.setRestaurant(restaurant1);

        menu2 = new Menu("Меню дня", meals2);
        menu2.setId(2);
        restaurant2 = new Restaurant(2, "Макдак", menu2);
        menu2.setRestaurant(restaurant2);

        menu3 = new Menu("Меню дня", meals3);
        menu3.setId(3);
        restaurant3 = new Restaurant(3, "Пицерия", menu3);
        menu3.setRestaurant(restaurant3);

        menu4 = new Menu("Меню дня", meals4);
        menu4.setId(4);
        restaurant4 = new Restaurant(4, "Закусочная", menu4);
        menu4.setRestaurant(restaurant4);

        menus.add(menu1);
        menus.add(menu2);
        menus.add(menu3);
        menus.add(menu4);
    }

    public static List<Restaurant> getTestRestaurants() {
        List<Restaurant> restaurants = new ArrayList();
        restaurants.add(restaurant1);
        restaurants.add(restaurant2);
        restaurants.add(restaurant3);
        restaurants.add(restaurant4);
        return restaurants;
    }
}
