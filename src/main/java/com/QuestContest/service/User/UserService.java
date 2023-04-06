package com.QuestContest.service.User;

import com.QuestContest.exception.ResourceNotFoundException;
import com.QuestContest.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class UserService {
    private final UserRepository userRepository;

    // Constructor
    public UserService(UserRepository userRepository, com.QuestContest.service.User.UserReader userReader) {
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

//    // Adding a new UserData to a specific User.
//    public User addDataToUser(int id, PatchUserDataRequest patchUserDataRequest) {
//        User user = getById(id);
//        UserData userData = new UserData(patchUserDataRequest.date(), patchUserDataRequest.weight(), patchUserDataRequest.height());
//        userData.setUser(user);
//        user.getUserData().add(userData);
//        return quizRepositoryRepository.save(user);
//    }

}
