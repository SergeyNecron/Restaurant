package ru.study.springboot.util;

import lombok.experimental.UtilityClass;
import ru.study.springboot.error.IllegalRequestDataException;
import ru.study.springboot.error.NotFoundException;

import java.time.LocalTime;
import java.util.Optional;

@UtilityClass
public class ValidationUtil {

    public static final LocalTime endTime = LocalTime.of(11, 0);

    public static void checkReVote(LocalTime timeReVote) {
        if (timeReVote.isAfter(endTime))
            throw new IllegalRequestDataException("you cannot re-vote after: " + endTime);
    }

    public static <T> T checkNotFoundWithId(Optional<T> optional, Integer id) {
        return checkNotFound(optional, "Entity with id = " + id + " not found");
    }

    public static <T> T checkNotFound(Optional<T> optional, String msg) {
        return optional.orElseThrow(() -> new NotFoundException(msg));
    }

    public static <T> void checkNotDuplicate(Optional<T> optional, String name) {
        if (optional.isPresent())
            throw new IllegalRequestDataException("Entity " + name + " duplicate");
    }
}