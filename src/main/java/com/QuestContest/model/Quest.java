package com.QuestContest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Quest {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JsonIgnore
    private User userQuest;
    @ManyToOne
    @JsonIgnore
    private User userQuestAnswered;
    @Column
    private int badge;
    @Column
    private String quest;
    @Column
    private String answer;

    public Quest(int badge, String quest, String answer) {
        this.badge = badge;
        this.quest = quest;
        this.answer = answer;
    }
}

