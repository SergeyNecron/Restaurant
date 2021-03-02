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
import java.time.LocalTime;
import java.util.Optional;

import static ru.study.springboot.util.ValidationUtil.checkNotFoundWithId;
import static ru.study.springboot.util.VoteUtil.checkNotDuplicate;
import static ru.study.springboot.util.VoteUtil.checkReVote;

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
        Optional<Vote> vote = voteRepository.getVoteByDateAndUser(dateTime.toLocalDate(), user);
        Restaurant restaurant = checkNotFoundWithId(restaurantRepository.getById(restaurantId), restaurantId);
        VoteOut voteOut;
        if (vote.isEmpty()) {
            voteOut = new VoteOut(voteRepository.save(new Vote(dateTime.toLocalDate(), user, restaurant)));
            log.info("create vote: date = {}, user_id = {}, restaurant_id = {}",
                    voteOut.getDate(), voteOut.getUserId(), voteOut.getRestaurantId());
        } else voteOut = new VoteOut(update(vote.get(), dateTime.toLocalTime(), restaurant));

        return ResponseEntity.ok(voteOut);
    }

    private Vote update(Vote vote, LocalTime time, Restaurant restaurant) {
        log.info("update vote: id = {}, date = {}, restaurant_id = {}",
                vote.getUser(), vote.getDate(), restaurant.id());
        checkReVote(time);
        checkNotDuplicate(vote.getRestaurant().id(), restaurant.id());
        vote.setRestaurant(restaurant);
        return voteRepository.save(vote);
    }
}
