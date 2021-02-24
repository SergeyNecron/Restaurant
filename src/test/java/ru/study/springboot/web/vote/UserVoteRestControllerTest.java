package ru.study.springboot.web.vote;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import ru.study.springboot.dto.VoteOut;
import ru.study.springboot.error.IllegalRequestDataException;
import ru.study.springboot.model.User;
import ru.study.springboot.web.TestUtil;
import ru.study.springboot.web.restaurant.AbstractRestaurantControllerTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.study.springboot.web.TestData.*;
import static ru.study.springboot.web.vote.UserVoteRestController.REST_URL_VOTE_USER;

class UserVoteRestControllerTest extends AbstractRestaurantControllerTest {
    private static final LocalDateTime checkTimeBefore = LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0));
    private static final LocalDateTime checkTimeAfter = LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 0));

    @Autowired
    private UserVoteRestController userVoteRestController;

    @Test
    void voteUser() throws Exception {
        checkVote(USER);
    }

    @Test
    void voteAdmin() throws Exception {
        checkVote(ADMIN);
    }

    @Test
    void checkDuplicateVoteForUserAfterEndTimeException() throws Exception {
        getMvcResultPut(USER, 1);
        assertThrows(IllegalRequestDataException.class, () -> userVoteRestController.saveOrUpdateOnDate(USER, 1, checkTimeBefore));
    }

    @Test
    void checkReVoteForUserAfterEndTimeException() throws Exception {
        getMvcResultPut(USER, 1);
        assertThrows(IllegalRequestDataException.class, () -> userVoteRestController.saveOrUpdateOnDate(USER, 2, checkTimeAfter));
    }

    @Test
    void checkReVoteForUserBeforeEndTime() throws Exception {
        getMvcResultPut(USER, 1);
        VoteOut voteOut = userVoteRestController.saveOrUpdateOnDate(USER, 2, checkTimeBefore).getBody();
        assertEquals(2, voteOut.getRestaurantId());
    }

    private void checkVote(User user) throws Exception {
        MvcResult action = getMvcResultPut(user, 1)
                .andExpect(status().isOk())
                .andReturn();
        VoteOut voteOut = TestUtil.readFromJsonMvcResult(action, VoteOut.class);
        assertEquals(1, voteOut.getRestaurantId());
    }

    private ResultActions getMvcResultPut(User user, Integer id) throws Exception {
        return super.getMvcResultPut(user, REST_URL_VOTE_USER + "/" + id);
    }

    @Test
    void votingUnAuth() throws Exception {
        getMvcResultGet(USER_NOT_REGISTRATION, 2)
                .andExpect(status().isUnauthorized());
    }
}