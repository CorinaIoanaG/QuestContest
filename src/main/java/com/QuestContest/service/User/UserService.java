package com.QuestContest.service.User;

import com.QuestContest.exception.ResourceNotFoundException;
import com.QuestContest.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class UserService {
    private final UserRepository quizRepositoryRepository;

    // Constructor
    public UserService(UserRepository quizRepository, com.QuestContest.service.User.UserReader userReader) {
        this.quizRepositoryRepository = quizRepository;
        quizRepository.saveAll(userReader.getUsers());
    }

    // Returns all users.
    public List<User> getAll() {
        return quizRepositoryRepository.findAll();
    }

    // Returns a User with a specific id.
    public User getById(Long id) {
        return quizRepositoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User missing ", id));
    }

//    // Updates name, town and contact for a specific User.
//    public User patch(int id, String name, String town, String contact) {
//        User userToBeUpdated = getById(id);
//        userToBeUpdated.setFullName(name);
//        userToBeUpdated.setTown(town);
//        userToBeUpdated.setContact(contact);
//        return quizRepositoryRepository.save(userToBeUpdated);
//    }

    // Deletes a specific User.
    public User deleteById(Long id) {
        User userToBeDeleted = getById(id);
        quizRepositoryRepository.deleteById(id);
        return userToBeDeleted;
    }

    // Adding a new User.
    public User add(User user) {
        return quizRepositoryRepository.save(user);
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
