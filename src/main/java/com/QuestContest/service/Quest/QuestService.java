package com.QuestContest.service.Quest;

import com.QuestContest.exception.ResourceNotFoundException;
import com.QuestContest.model.Quest;

import java.util.List;

public class QuestService {
    private final QuestRepository questRepository;

    // Constructor
    public QuestService(QuestRepository questRepository) {
        this.questRepository = questRepository;
    }

    // Returns all quests.
    public List<Quest> getAll() {
        return questRepository.findAll();
    }

    // Returns a quest with a specific id.
    public Quest getById(Long id) {
        return questRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quest missing ", id));
    }

    //Add a new quest
    public Quest add(Quest quest) {
        return questRepository.save(quest);
    }
}
