package ru.study.springboot.util;

import lombok.experimental.UtilityClass;
import ru.study.springboot.error.IllegalRequestDataException;

import java.time.LocalTime;

@UtilityClass
public class VoteUtil {

    public static final LocalTime endTime = LocalTime.of(11, 0);

    public static void checkReVote(LocalTime timeReVote) {
        if (timeReVote.isAfter(endTime))
            throw new IllegalRequestDataException("you cannot re-vote after: " + endTime);
    }
}