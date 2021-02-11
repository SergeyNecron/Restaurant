package ru.study.springboot.web.vote;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.study.springboot.error.IllegalRequestDataException;
import ru.study.springboot.to.VoteOut;
import ru.study.springboot.web.restaurant.AbstractRestaurantControllerTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.study.springboot.web.TestData.USER;

class UserVoteRestControllerTest extends AbstractRestaurantControllerTest {
    private static final LocalDateTime checkTimeBefore = LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0));
    private static final LocalDateTime checkTimeAfter = LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 0));

    @Autowired
    private UserVoteRestController userVoteRestController;

    @Test
    void checkDuplicateVoteForUserAfterEndTimeException() throws Exception {
        getMvcResultPut(USER, 1);
        assertThrows(IllegalRequestDataException.class, () -> userVoteRestController.vote(USER, 1, checkTimeBefore));
    }

    @Test
    void checkReVoteForUserAfterEndTimeException() throws Exception {
        getMvcResultPut(USER, 1);
        assertThrows(IllegalRequestDataException.class, () -> userVoteRestController.vote(USER, 2, checkTimeAfter));
    }

    @Test
    void checkReVoteForUserBeforeEndTime() throws Exception {
        getMvcResultPut(USER, 1);
        VoteOut voteOut = userVoteRestController.vote(USER, 2, checkTimeBefore).getBody();
        assertEquals(2, voteOut.getRestaurantId());
    }
}