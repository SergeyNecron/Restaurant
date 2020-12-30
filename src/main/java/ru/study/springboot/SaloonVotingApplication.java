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

import java.util.*;


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
        Map<String, Double> meals = new HashMap<>();
        meals.put("Бизнес-ланч", 300.0);

        Menu menu = new Menu(null,"София", meals);
        User user = new User("admin@ya.ru", "password", Role.ROLE_ADMIN, Role.ROLE_USER);
        user.setVail(20);
        user.setMenus(Collections.singletonList(menu));
        userRepository.save(user);
        menu.setUser(user);
        menuRepository.save(menu);
    }
}
