package com.questcontest.service.quest;

import com.questcontest.controller.dto.PostQuestRequest;
import com.questcontest.exception.ResourceNotFoundException;
import com.questcontest.model.Quest;
import com.questcontest.model.User;
import com.questcontest.service.user.UserRepository;
import com.questcontest.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor

public class QuestService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final QuestRepository questRepository;

    // Returns quest with a specific id.
    public Quest getById(Long id) {
        return questRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quest missing ", id));
    }
    // Adds a new Quest from a specific User.
    public User addQuestfromUser(Long id, PostQuestRequest postQuestRequest) {
        User user = userService.getById(id);
        if (user.getTokens() < 100) {
            throw new ResourceNotFoundException("Not enough tokens to propose quest", id);
        }
        if ((postQuestRequest.badge() < 1 && postQuestRequest.badge() <= user.getBadge())
                || postQuestRequest.quest() == null || postQuestRequest.answer() == null) {
            throw new RuntimeException("Quest has null fields or bad inputs");
        }
        if (user.getQuests().stream().anyMatch(quest ->
                quest.getQuestDescription().equalsIgnoreCase(postQuestRequest.quest()))) {
            throw new RuntimeException("Quest already exists");
        }
        Quest quest = new Quest(postQuestRequest.badge(), postQuestRequest.quest(), postQuestRequest.answer());
        quest.setUserQuest(user);
        user.getQuests().add(quest);
        updateUserTokens(user, 5 * postQuestRequest.badge());
        calculateUserBadge(user);
        return userRepository.save(user);
    }

    public void calculateUserBadge(User user) {
        //noinspection IntegerDivisionInFloatingPointContext
        user.setBadge((int) Math.ceil(user.getTokens() / 100));
    }

    public void updateUserTokens(User user, int amount) {
        user.setTokens(user.getTokens() + amount);
    }

    // Returns unresolved quests for a specific User.
    public List<Quest> getUnresolvedQuestsForUser(Long id) {
        User user = userService.getById(id);
        return questRepository.findAll().stream()
                .filter(quest -> !quest.getUserQuest().equals(user)
                        && !quest.getUserQuestAnswered().equals(user))
                .toList();
    }

    // Returns a random unresolved quest for a specific user.
    public Quest getRandomUnresolvedQuestForUser(List<Quest> quests) {
        Random random = new Random();
        Long questId = random.nextLong(quests.size());
        return quests.get(Math.toIntExact(questId));
    }


    // Saves answer for a user quest, if is correct.
    public User resolveQuestForUser(Long userId, Long questId, String answer) {
        User user = userService.getById(userId);
        Quest quest = getById(questId);
        if (user.getTokens() < quest.getBadge()) {
            throw new RuntimeException("Not enough badges for this quest");
        }
        if (quest.getAnswer().equalsIgnoreCase(answer)) {
            updateUserTokens(user, quest.getBadge());
            calculateUserBadge(user);
            quest.setUserQuestAnswered(user);
            updateUserTokens(quest.getUserQuest(), - quest.getBadge());
        } else {
            updateUserTokens(user, - quest.getBadge());
            calculateUserBadge(user);
            throw new RuntimeException("Wrong answer");
        }
        return userRepository.save(user);
    }

}
