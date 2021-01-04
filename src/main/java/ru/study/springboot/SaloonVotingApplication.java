package ru.study.springboot;

import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.study.springboot.model.Menu;
import ru.study.springboot.model.Role;
import ru.study.springboot.model.User;
import ru.study.springboot.repository.MenuRepository;
import ru.study.springboot.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SpringBootApplication
@AllArgsConstructor
public class SaloonVotingApplication implements ApplicationRunner {
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;

    public static void main(String[] args) {
        SpringApplication.run( SaloonVotingApplication.class, args);
    }
    @Override
    public void run(ApplicationArguments args) {
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

        User user = new User("user@ya.ru", "{noop}password", Role.USER);
        User admin= new User("admin@ya.ru", "{noop}password", Role.ADMIN, Role.USER);
        user.setVail(4);
        admin.setVail(4);
        userRepository.save(user);
        userRepository.save(admin);

        menuRepository.saveAll(menus);
    }
}
