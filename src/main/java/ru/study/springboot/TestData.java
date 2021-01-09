package ru.study.springboot;

import ru.study.springboot.model.Menu;
import ru.study.springboot.to.MenuRating;
import ru.study.springboot.util.MenuUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TestData {

    public static List<Menu> getTestMenus() {
        Map<String, Double> meals1 = new HashMap<>();
        Map<String, Double> meals2 = new HashMap<>();
        Map<String, Double> meals3 = new HashMap<>();
        Map<String, Double> meals4 = new HashMap<>();

        meals1.put("Бизнес-ланч",100.0);
        meals1.put("Тёплые ролы",99.0);
        meals1.put("Салат Цезарь",99.0);
        meals1.put("Лимонад",90.0);
        meals1.put("Пицца",58.9);
        meals1.put("Мороженное",8.0);

        meals2.put("Суп",50.0);
        meals2.put("Отбивная",20.0);
        meals2.put("Картофельное пюре",80.0);
        meals2.put("Амлет",70.0);
        meals2.put("Хачапури",50.0);
        meals2.put("Чай",50.0);

        meals3.put("Ланч",200.0);
        meals3.put("Ролы", 99.0);
        meals3.put("Салат фруктовый", 99.0);
        meals3.put("Кофе",60.0);
        meals3.put("Бургер", 70.0);
        meals3.put("Гамбургер", 60.0);

        meals4.put("Салат Цезарь", 80.0);
        meals4.put("Спагетти с соусом", 50.0);
        meals4.put("Щи", 40.0);
        meals4.put("Ножки индейки", 500.0);
        meals4.put("Горячий шоколад", 80.0);
        meals4.put("Кофе", 50.5);

        List<Menu> menus = new ArrayList();
        menus.add(new Menu("София", meals1));
        menus.add(new Menu("Макдак", meals2));
        menus.add(new Menu("Пицерия", meals3));
        menus.add(new Menu("Закусочная", meals4));

        return menus;
    }
    public static List<MenuRating> getTestMenusTo() {
       return getTestMenus()
                .stream()
                .map(it-> MenuUtil.to(it,0))
                .collect(Collectors.toList());
    }
}