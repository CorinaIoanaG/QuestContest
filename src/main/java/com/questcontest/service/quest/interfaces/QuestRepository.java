package com.questcontest.service.quest.interfaces;

import com.questcontest.model.Quest;
import com.questcontest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestRepository extends JpaRepository<Quest, Long> {

    List<Quest> findByQuestDescription(String questDescription);

    List<Quest> findByUserQuestProposed(User userQuestProposed);

    List<Quest> findByAvailable(boolean available);


}

