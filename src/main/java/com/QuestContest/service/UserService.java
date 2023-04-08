package com.QuestContest.service;

import com.QuestContest.controller.dto.PostQuestRequest;
import com.QuestContest.exception.ResourceNotFoundException;
import com.QuestContest.model.Quest;
import com.QuestContest.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service

public class UserService {
    private final UserRepository userRepository;

    // Constructor
    public UserService(UserRepository userRepository, UserReader userReader) {
        this.userRepository = userRepository;
        userRepository.saveAll(userReader.getUsers());
    }

    // Returns all users.
    public List<User> getAll() {
        return userRepository.findAll();
    }

    // Returns User with given name and password.
    public User getByNameAndPass(String name, String pass) {
        return userRepository.findAll().stream().filter(user ->
                        Objects.equals(user.getName(), name) && Objects.equals(user.getPass(), pass))
                .findFirst().get();
    }

    // Returns a User with a specific id.
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User missing ", id));
    }

    // Updates a specific User.
    public User patch(Long id, String name, String pass, String fullName, String email) {
        User userToBeUpdated = getById(id);
        userToBeUpdated.setName(name);
        userToBeUpdated.setPass(pass);
        userToBeUpdated.setFullName(fullName);
        userToBeUpdated.setEmail(email);
        return userRepository.save(userToBeUpdated);
    }

    // Deletes a specific User.
    public User deleteById(Long id) {
        User userToBeDeleted = getById(id);
        userRepository.deleteById(id);
        return userToBeDeleted;
    }

    // Adds a new User.
    public User add(User user) {
        if (user.getName() == null || user.getPass() == null ||
                user.getFullName() == null || user.getEmail() == null) {
            throw new RuntimeException("User has null fields");
        }
        if (userRepository.findAll().stream().anyMatch(user1 -> user1.getName().equals(user.getName()))) {
            throw new RuntimeException("User Name already exists");
        }
        user.setBadge(1);
        return userRepository.save(user);
    }

    // Adds a new Quest from a specific User.
    public User addQuestfromUser(Long id, PostQuestRequest postQuestRequest) {
        User user = getById(id);
        if (user.getTokens() < 100) {
            throw new ResourceNotFoundException("Not enough tokens to propose quest", id);
        }
        if ((postQuestRequest.badge() < 1 && postQuestRequest.badge() <= user.getBadge())
                || postQuestRequest.quest() == null || postQuestRequest.answer() == null) {
            throw new RuntimeException("Quest has null fields or bad inputs");
        }
        if (user.getQuests().stream().anyMatch(quest -> quest.getQuest().equalsIgnoreCase(postQuestRequest.quest()))) {
            throw new RuntimeException("Quest already exists");
        }
        Quest quest = new Quest(postQuestRequest.badge(), postQuestRequest.quest(), postQuestRequest.answer());
        quest.setUserQuest(user);
        user.getQuests().add(quest);
        user.setTokens(user.getTokens() + 5 * postQuestRequest.badge());
        //noinspection IntegerDivisionInFloatingPointContext
        user.setBadge((int) Math.ceil(user.getTokens()/100));
        return userRepository.save(user);
    }

}
