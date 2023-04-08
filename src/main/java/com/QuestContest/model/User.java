package com.QuestContest.model;

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
    private String name;
    @Column
    private String pass;
    @Column
    private String fullName;
    @Column
    private String email;
    @Column
    private int badge;
    @Column
    private int tokens;
    @Column
    private int ranking;
    @OneToMany(mappedBy = "userQuest", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private List<Quest> quests;
    @OneToMany(mappedBy = "userQuestAnswered", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private List<Quest> questsAnswered;

}
