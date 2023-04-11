package com.questcontest.service.quest.classes;

import com.questcontest.controller.dto.PostQuestRequest;
import com.questcontest.model.Quest;
import com.questcontest.model.User;
import com.questcontest.service.quest.interfaces.QuestRepository;
import com.questcontest.service.quest.interfaces.QuestServiceInterface;
import com.questcontest.service.user.interfaces.UserRepository;
import com.questcontest.service.user.interfaces.UserServiceInterface;
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
        quest.setUserQuestProposed(user);
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
        User userProposedQuest = quest.getUserQuestProposed();
        if (quest.getAnswer().equalsIgnoreCase(answer) || quest.getAnswer().contains(answer)) {
            user.setTokens(userService.calculateUserTokens(user, quest.getTokens()));
            userProposedQuest.setTokens(userService.calculateUserTokens(userProposedQuest, -quest.getTokens()));
        } else {
            user.setTokens(userService.calculateUserTokens(user, -quest.getTokens()));
            userRepository.save(user);
            userProposedQuest.setTokens(userService.calculateUserTokens(userProposedQuest, quest.getTokens()));
            userRepository.save(userProposedQuest);
            throw new RuntimeException("Wrong answer");
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
