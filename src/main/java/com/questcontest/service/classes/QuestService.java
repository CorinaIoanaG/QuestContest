package com.questcontest.service.classes;

import com.questcontest.controller.dto.PostQuestRequest;
import com.questcontest.model.Quest;
import com.questcontest.model.User;
import com.questcontest.service.interfaces.QuestRepository;
import com.questcontest.service.interfaces.QuestServiceInterface;
import com.questcontest.service.interfaces.UserRepository;
import com.questcontest.service.interfaces.UserServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor

public class QuestService implements QuestServiceInterface {

    private final UserRepository userRepository;
    private final UserServiceInterface userService;
    private final QuestRepository questRepository;


    // Returns quest with a specific questId.
    public Quest getById(Long questId) {
        return questRepository.findById(questId)
                .orElseThrow(() -> new RuntimeException("Quest missing "));
    }

    // Adds a new Quest from a specific User.
    public User addQuestFromUser(Long userId, PostQuestRequest postQuestRequest) {
        User user = userService.getById(userId);
        if (user.getTokens() < 10) {
            throw new RuntimeException("Not enough tokens to propose quest");
        }
        if ((postQuestRequest.tokens() < 1) || (postQuestRequest.tokens() > user.getBadge())
                || (postQuestRequest.questDescription() == null) || (postQuestRequest.answer() == null)) {
            throw new RuntimeException("Quest has null fields or bad inputs");
        }
        if (!questRepository.findByQuestDescription(postQuestRequest.questDescription()).isEmpty()) {
            throw new RuntimeException("Quest already exists");
        }
        Quest quest = new Quest(postQuestRequest.tokens(), postQuestRequest.questDescription(), postQuestRequest.answer());
        quest.setCreatorUser(user);
        user.getQuestsProposed().add(quest);
        user.setTokens(userService.calculateUserTokens(user, 5 * postQuestRequest.tokens()));
        user.setBadge(userService.calculateUserBadge(user));
        return userRepository.save(user);
    }

    // Returns unresolved and available quests for a specific User.
    public List<Quest> getQuestsForUser(Long userId) {
        User user = userService.getById(userId);
        return questRepository.findByAvailable(true).stream()
                .filter(quest -> quest.getTokens() <= user.getTokens())
                .toList();
    }

    //Returns a quest from List for a specific user id
    public Quest getAQuestForUser(Long userId) {
        List<Quest> questsForUser = getQuestsForUser(userId);
        if (questsForUser.size() < 1) {
            throw new RuntimeException("No questions for this user");
        }
        Random random = new Random();
        int questId = random.nextInt(questsForUser.size());
        return questsForUser.get(questId);
    }

    // Saves answer for a user quest, if it's correct.
    public User resolveQuestForUser(Long userId, Long questId, String answer) {
        User user = userService.getById(userId);
        Quest quest = getById(questId);
        User creatorUser = quest.getCreatorUser();
        if (quest.getAnswer().equalsIgnoreCase(answer) || quest.getAnswer().contains(answer)) {
            user.setTokens(userService.calculateUserTokens(user, quest.getTokens()));
            updateAvailabilityOfQuests(user);
            creatorUser.setTokens(userService.calculateUserTokens(creatorUser, -quest.getTokens()));
            updateAvailabilityOfQuests(creatorUser);
        } else {
            user.setTokens(userService.calculateUserTokens(user, -quest.getTokens()));
            updateAvailabilityOfQuests(user);
            userRepository.save(user);
            creatorUser.setTokens(userService.calculateUserTokens(creatorUser, quest.getTokens()));
            updateAvailabilityOfQuests(creatorUser);
            userRepository.save(creatorUser);
            throw new RuntimeException("Wrong answer");
        }
        user.setBadge(userService.calculateUserBadge(creatorUser));
        userRepository.save(creatorUser);
        user.setBadge(userService.calculateUserBadge(user));
        return userRepository.save(user);
    }

    public void updateAvailabilityOfQuests(User user) {
        List<Quest> questsFromUser = questRepository.findByCreatorUser(user);
        for (Quest quest : questsFromUser) {
            if (quest.isAvailable() && quest.getTokens() >= user.getTokens()) {
                quest.setAvailable(false);
            }
            if (!quest.isAvailable() && quest.getTokens() < user.getTokens()) {
                quest.setAvailable(true);
            }
        }
    }

}
