package com.QuestContest.service.User;

import com.QuestContest.model.Quest;
import com.QuestContest.model.User;
import com.QuestContest.service.Quest.QuestService;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository

public class UserReader {

    QuestService questService;

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
    private User lineToUser(String line) throws ParseException {
        String[] userParts = line.split("\\|");
        User user = new User(0L, userParts[0], userParts[1], userParts[2], userParts[3], Integer.parseInt(userParts[4]),
                Integer.parseInt(userParts[5]), List.of(),List.of());
//        questId.forEach(questId1 -> questService.getById(questId1).setUserQuest(user));
//        questAnsweredId.forEach(questAnsweredId1 -> questService.getById(questAnsweredId1).setUserQuest(user));
        return user;
    }


}
