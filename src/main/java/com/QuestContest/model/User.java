package com.QuestContest.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class User {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String userName;
    @Column
    private String password;
    @Column
    private String userFullName;
    @Column
    private String email;
    @Column
    private int tokens;
    @Column
    private int ranking;
    @Nullable
    @OneToMany(mappedBy = "userQuest", cascade = {CascadeType.DETACH}, fetch = FetchType.EAGER)
    private List<Quest> quests;
    @Nullable
    @OneToMany(mappedBy = "userQuestAnswered", cascade = {CascadeType.DETACH}, fetch = FetchType.EAGER)
    private List<Quest> questsAnswered;
}
