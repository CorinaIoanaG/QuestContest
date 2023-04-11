package com.questcontest.service.user.interfaces;

import com.questcontest.model.Quest;
import com.questcontest.model.User;

import java.text.ParseException;
import java.util.List;

public interface UserReaderInterface {
    List<User> getUsers();

    User lineToUser(String line) throws ParseException;

    Quest parseQuest(String questItem);

}
