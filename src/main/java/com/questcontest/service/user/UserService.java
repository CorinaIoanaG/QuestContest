package com.questcontest.service.user;

import com.questcontest.exception.ResourceNotFoundException;
import com.questcontest.model.User;
import org.springframework.stereotype.Service;

import java.util.Comparator;
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
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Returns User with given name and password.
    public User getByNameAndPass(String name, String pass) {
        return userRepository.findByNameAndPass(name, pass).get(0);
    }

    // Returns a User with a specific id.
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User missing ", id));
    }

    // Updates rankings for all users and returns users.
    public List<User> updateAllRankings() {
        List<User> result = getAllUsers().stream()
                .sorted(Comparator.comparingInt(User::getTokens).reversed())
                .toList();
        Long i = 1L;
        for (User user : result) {
            user.setRanking(i++);
            userRepository.save(user);
        }
        return result;
    }

    // Updates a specific User.
    public User patch(Long id, String name, String pass, String fullName, String email) {
        if (name.isBlank() || pass.isBlank() || fullName.isBlank() || email.isBlank()) {
            throw new RuntimeException("User has null fields");
        }
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
        if (!userRepository.findByName(user.getName()).isEmpty()) {
            throw new RuntimeException("User Name already exists");
        }
        user.setBadge(1);
        user.setTokens(20);
        return userRepository.save(user);
    }

    public int calculateUserTokens(User user, int amount) {
        return user.getTokens() + amount;
    }

    public int calculateUserBadge(User user) {
        return user.getTokens() / 100 + 1;
    }
}
