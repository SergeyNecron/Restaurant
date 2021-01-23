package ru.study.springboot.to;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.study.springboot.model.Vote;

import java.time.LocalDate;

@Data
@Value
@EqualsAndHashCode()
public class VoteTo {
    LocalDate date;
    Integer user;
    String menu;

    public VoteTo(LocalDate date, Integer user, String menu) {
        this.date = date;
        this.user = user;
        this.menu = menu;
    }

    public VoteTo(Vote votes) {
        this.date = votes.getDate();
        this.user = votes.getUser().id();
        this.menu = votes.getMenu().getSaloon();
    }
}