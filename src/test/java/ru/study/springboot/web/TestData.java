package ru.study.springboot.web;

import ru.study.springboot.model.Meal;
import ru.study.springboot.model.Menu;
import ru.study.springboot.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class TestData {

    static List<Meal> meals1 = new ArrayList();
    static List<Meal> meals2 = new ArrayList();
    static List<Meal> meals3 = new ArrayList();
    static List<Meal> meals4 = new ArrayList();

    static Menu menu1;
    static Menu menu2;
    static Menu menu3;
    static Menu menu4;

    static {
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
        menu2 = new Menu("Меню дня", meals2);
        menu3 = new Menu("Меню дня", meals3);
        menu4 = new Menu("Меню дня", meals4);
        menu1.setId(1);
    }

    public static List<Menu> getTestMenus() {
        List<Menu> menus = new ArrayList();
        menus.add(new Menu("Меню дня", meals1));
        menus.add(new Menu("Меню дня", meals2));
        menus.add(new Menu("Меню дня", meals3));
        menus.add(new Menu("Меню дня", meals4));
        return menus;
    }

    public static List<Restaurant> getTestRestaurants() {
        List<Restaurant> menus = new ArrayList();
        menus.add(new Restaurant("София", menu1));
        menus.add(new Restaurant("Макдак", menu2));
        menus.add(new Restaurant("Пицерия", menu3));
        menus.add(new Restaurant("Закусочная", menu4));
        return menus;
    }

    public static Restaurant getRestaurant() {
        return new Restaurant("София", menu1);
    }

}
