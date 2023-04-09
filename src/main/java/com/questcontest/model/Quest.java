package com.questcontest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String questDescription;
    @Column
    private String answer;
    @Column
    private boolean available;

    public Quest(int badge, String questDescription, String answer) {
        this.badge = badge;
        this.questDescription = questDescription;
        this.answer = answer;
        this.available = true;
    }
}

