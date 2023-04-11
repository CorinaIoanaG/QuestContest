package com.questcontest.service.user.classes;

import com.questcontest.model.Quest;
import com.questcontest.model.User;
import com.questcontest.service.user.interfaces.UserReaderInterface;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@Repository

public class UserReader implements UserReaderInterface {

    // Reads users from file and returns a list with users.
    public List<User> getUsers() {
        try {
            return Files.lines(Path.of("src/main/resources/usersdata.txt"))
                    .map(line -> {
                        try {
                            return lineToUser(line);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Splits a line read from file in User parts and returns users, one by one.
    public User lineToUser(String line) throws ParseException {
        String[] userParts = line.split("\\|");
        User user = new User(0L, userParts[0], userParts[1], userParts[2], userParts[3], Integer.parseInt(userParts[4]),
                Integer.parseInt(userParts[5]), Long.parseLong(userParts[6]),
                userParts.length > 7 ? List.of(parseQuest(userParts[7])):List.of());
        user.getQuestsProposed().forEach(quest -> quest.setUserQuestProposed(user));
        return user;
    }

    // Splits a String in Quest parts.
    public Quest parseQuest(String questItem){
        String[] questParts = questItem.split("\\^");
        return new Quest(Integer.parseInt(questParts[0]),questParts[1], questParts[2]);
    }

}
