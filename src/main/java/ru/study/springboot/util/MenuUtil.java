package ru.study.springboot.util;

import org.springframework.lang.Nullable;
import ru.study.springboot.to.MenuRating;

import ru.study.springboot.model.Menu;

public class MenuUtil {
    public static MenuRating to(Menu menu, Integer rating) {
        return new MenuRating(menu.getSaloon(), menu.getMeals(), rating);
    }

    public static <T extends Comparable<T>> boolean isBetweenHalfOpen(T value, @Nullable T start, @Nullable T end) {
        return (start == null || value.compareTo(start) >= 0) && (end == null || value.compareTo(end) < 0);
    }
}