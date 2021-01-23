package ru.study.springboot;

import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.study.springboot.repository.MenuRepository;

import static ru.study.springboot.StartData.getTestMenus;

@SpringBootApplication
@AllArgsConstructor
public class SaloonVotingApplication implements ApplicationRunner {
    private final MenuRepository menuRepository;

    public static void main(String[] args) {
        SpringApplication.run(SaloonVotingApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        menuRepository.saveAll(getTestMenus());
    }
}
