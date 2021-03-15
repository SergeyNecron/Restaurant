package ru.study.springboot.web.vote;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.study.springboot.AuthUser;
import ru.study.springboot.dto.VoteOut;
import ru.study.springboot.model.Restaurant;
import ru.study.springboot.model.User;
import ru.study.springboot.model.Vote;
import ru.study.springboot.repository.RestaurantRepository;
import ru.study.springboot.repository.VoteRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static ru.study.springboot.util.ValidationUtil.checkReVote;

@RestController
@RequestMapping(value = UserVoteRestController.REST_URL_VOTE_USER)
@Slf4j
@AllArgsConstructor
@Api(tags = "User vote controller")
public class UserVoteRestController {
    static final String REST_URL_VOTE_USER = "/rest/user/vote";

    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;

    @PutMapping("/{restaurant_id}")
    public ResponseEntity<VoteOut> saveOrUpdate(@AuthenticationPrincipal AuthUser authUser, @PathVariable int restaurant_id) {
        User user = authUser.getUser();
        return saveOrUpdateOnDate(user, restaurant_id, LocalDateTime.now());
    }

    public ResponseEntity<VoteOut> saveOrUpdateOnDate(User user, int restaurantId, LocalDateTime dateTime) {
        Optional<Vote> voteOpt = voteRepository.getVoteByDateAndUser(dateTime.toLocalDate(), user);
        Restaurant restaurant = restaurantRepository.getExisted(restaurantId);
        Vote vote;
        if (voteOpt.isEmpty()) {
            vote = new Vote(dateTime.toLocalDate(), user, restaurant);
            log.info("create : {}", vote);
        } else {
            vote = voteOpt.get();
            vote.setRestaurant(restaurant);
            vote.setUser(user);
            checkReVote(dateTime.toLocalTime());
            log.info("update : {}", vote);
        }
        voteRepository.save(vote);
        return ResponseEntity.ok(new VoteOut(vote, restaurantId));
    }
}
