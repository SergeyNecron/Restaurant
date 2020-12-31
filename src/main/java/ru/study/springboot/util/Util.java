package ru.study.springboot.util;

import ru.study.springboot.to.MenuTo;

import ru.study.springboot.model.Menu;

public class Util {
    public static MenuTo to(Menu menu, Integer rating) {
        return new MenuTo(menu.getSaloon(), menu.getMeals(), rating);
    }
}