package com.QuestContest.service;

import com.QuestContest.controller.dto.PatchQuestRequest;
import com.QuestContest.exception.ResourceNotFoundException;
import com.QuestContest.model.Quest;
import com.QuestContest.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

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

    // Adding a new User.
    public User add(User user) {
        return userRepository.save(user);
    }

    // Adding a new Quest from a specific User.
    public User addQuestfromUser(Long id, PatchQuestRequest patchQuestRequest) {
        User user = getById(id);
        Quest quest = new Quest(patchQuestRequest.badge(), patchQuestRequest.quest(), patchQuestRequest.answer());
        quest.setUserQuest(user);
        user.getQuests().add(quest);
        return userRepository.save(user);
    }

}
