package com.questcontest.service.interfaces;

import com.questcontest.controller.dto.PostQuestRequest;
import com.questcontest.model.Quest;
import com.questcontest.model.User;

import java.util.List;

public interface QuestServiceInterface {

    Quest getById(Long questId);

     User addQuestFromUser(Long userId, PostQuestRequest postQuestRequest);

     List<Quest> getQuestsForUser(Long userId);

     Quest getAQuestForUser(Long userId);

     User resolveQuestForUser(Long userId, Long questId, String answer);

     void updateAvailabilityOfQuests(User user) throws RuntimeException;
}
