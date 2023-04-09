package com.QuestContest.service;

import com.QuestContest.controller.dto.PostQuestRequest;
import com.QuestContest.exception.ResourceNotFoundException;
import com.QuestContest.model.Quest;
import com.QuestContest.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class QuestService{

    private final UserRepository userRepository;
    private final UserService userService;

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
        if (user.getQuests().stream().anyMatch(quest -> quest.getQuest().equalsIgnoreCase(postQuestRequest.quest()))) {
            throw new RuntimeException("Quest already exists");
        }
        Quest quest = new Quest(postQuestRequest.badge(), postQuestRequest.quest(), postQuestRequest.answer());
        quest.setUserQuest(user);
        user.getQuests().add(quest);
        user.setTokens(user.getTokens() + 5 * postQuestRequest.badge());
        //noinspection IntegerDivisionInFloatingPointContext
        user.setBadge((int) Math.ceil(user.getTokens()/100));
        return userRepository.save(user);
    }

}
