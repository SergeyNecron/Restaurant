package ru.study.springboot.util;

import ru.study.springboot.model.Menu;
import ru.study.springboot.to.MenuTo;

public class MenuUtil {
    public static MenuTo to(Menu menu, Integer rating) {
        return new MenuTo(menu.getSaloon(), menu.getMeals(), rating);
    }

    public static Menu toMenu(MenuTo menuTo) {
        return new Menu(menuTo.getNameSaloon(), menuTo.getMeals());
    }


}