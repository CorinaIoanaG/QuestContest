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


    // Returns all quests.
    public List<Quest> getAllQuests() {
        return questRepository.findAll();
    }

    // Returns quest with a specific id.
    public Quest getById(Long id) {
        return questRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quest missing ", id));
    }

    // Adds a new Quest from a specific User.
    public User addQuestfromUser(Long id, PostQuestRequest postQuestRequest) {
        User user = userService.getById(id);
        if (user.getTokens() < 10) {
            throw new ResourceNotFoundException("Not enough tokens to propose quest", id);
        }
        if ((postQuestRequest.tokens() < 1) || (postQuestRequest.tokens() > user.getBadge())
                || (postQuestRequest.questDescription() == null) || (postQuestRequest.answer() == null)) {
            throw new RuntimeException("Quest has null fields or bad inputs");
        }
        if (!questRepository.findByQuestDescription(postQuestRequest.questDescription()).isEmpty()) {
            throw new RuntimeException("Quest already exists");
        }
        Quest quest = new Quest(postQuestRequest.tokens(), postQuestRequest.questDescription(), postQuestRequest.answer());
        quest.setUserQuestProposed(user);
        user.getQuests().add(quest);
        user.setTokens(userService.calculateUserTokens(user, 5 * postQuestRequest.tokens()));
        user.setBadge(userService.calculateUserBadge(user));
        return userRepository.save(user);
    }

    // Returns unresolved and available quests for a specific User.
    public List<Quest> getUnresolvedQuestsForUser(Long id) {
        User user = userService.getById(id);
        return questRepository.findByAvailable(true).stream()
                .filter(quest -> !(quest.getUserQuestProposed().equals(user))
                        || !quest.getUserQuestAnswered().equals(user))
                .toList();
    }

    //Returns a quest from List for a specific user id
    public Quest getAQuestForUser(Long userId) {
        Random random = new Random();
        List<Quest> questsForUser = getUnresolvedQuestsForUser(userId);
        int questId = random.nextInt(questsForUser.size());
        return questsForUser.get(questId);
    }

    // Saves answer for a user quest, if it's correct.
    public User resolveQuestForUser(Long userId, Long questId, String answer) {
        User user = userService.getById(userId);
        Quest quest = getById(questId);
        User userProposedQuest = quest.getUserQuestProposed();
        if (user.getTokens() < quest.getTokens()) {
            throw new RuntimeException("Not enough tokens for this quest");
        }
        if (quest.getAnswer().equalsIgnoreCase(answer) || quest.getAnswer().contains(answer)) {
            user.setTokens(userService.calculateUserTokens(user, quest.getTokens()));
            user.getQuestsAnswered().add(quest);
            userProposedQuest.setTokens(userService.calculateUserTokens(userProposedQuest, -quest.getTokens()));
        } else {
            user.setTokens(userService.calculateUserTokens(user, -quest.getTokens()));
            userProposedQuest.setTokens(userService.calculateUserTokens(userProposedQuest, quest.getTokens()));
        }
        updateAvailabilityOfQuests(userProposedQuest, userProposedQuest.getTokens());
        user.setBadge(userService.calculateUserBadge(userProposedQuest));
        userRepository.save(userProposedQuest);
        updateAvailabilityOfQuests(user, user.getTokens());
        user.setBadge(userService.calculateUserBadge(user));
        return userRepository.save(user);
    }

    public void updateAvailabilityOfQuests(User user, int tokens) {
        List<Quest> questsFromUser = questRepository.findByUserQuestProposed(user);
        for (Quest quest : questsFromUser) {
            if (quest.isAvailable() && quest.getTokens() > tokens) {
                quest.setAvailable(false);
            }
            if (!quest.isAvailable() && quest.getTokens() < tokens) {
                quest.setAvailable(true);
            }
        }
    }

}
