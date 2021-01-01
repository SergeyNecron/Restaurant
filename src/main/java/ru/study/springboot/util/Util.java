package ru.study.springboot.util;

import org.springframework.lang.Nullable;
import ru.study.springboot.to.MenuTo;

import ru.study.springboot.model.Menu;

public class Util {
    public static MenuTo to(Menu menu, Integer rating) {
        return new MenuTo(menu.getSaloon(), menu.getMeals(), rating);
    }

    public static <T extends Comparable<T>> boolean isBetweenHalfOpen(T value, @Nullable T start, @Nullable T end) {
        return (start == null || value.compareTo(start) >= 0) && (end == null || value.compareTo(end) < 0);
    }
}