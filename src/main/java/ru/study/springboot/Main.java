package ru.study.springboot;

import ru.study.springboot.model.Meal;
import ru.study.springboot.model.Menu;
import ru.study.springboot.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Meal meal1 = new Meal("Бизнес-ланч", 100.0);
        Meal meal2 = new Meal("Бизнес-ланч", 100.0);
        List<Meal> list1 = new ArrayList();
        List<Meal> list2 = new ArrayList();
        meal1.setId(1);
        list1.add(meal1);
        list2.add(meal2);
        Menu menu1 = new Menu("w", list1);
        Menu menu2 = new Menu("w", list2);
        menu1.setId(1);
        Restaurant restaurant1 = new Restaurant("fsdf", menu1);
        Restaurant restaurant2 = new Restaurant("fsdf", menu2);
        meal1.setMenu(menu1);
        menu1.setMeals(List.of(meal1));
        menu1.setRestaurant(restaurant1);
        restaurant1.setId(1);
        restaurant1.setMenus(List.of(menu1));

        System.out.println(meal1.equals(meal2));
        System.out.println(menu1.equals(menu2));
        System.out.println(restaurant1.equals(restaurant2));
    }
}