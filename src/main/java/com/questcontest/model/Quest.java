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
    private User userQuestProposed;
    @Column
    private int tokens;
    @Column
    private String questDescription;
    @Column
    private String answer;
    @Column
    private boolean available;

    public Quest(int tokens, String questDescription, String answer) {
        this.tokens = tokens;
        this.questDescription = questDescription;
        this.answer = answer;
        this.available = true;
    }
}

