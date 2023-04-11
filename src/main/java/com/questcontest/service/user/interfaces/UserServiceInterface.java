package com.questcontest.service.user.interfaces;

import com.questcontest.model.User;

import java.util.List;

public interface UserServiceInterface {

    List<User> getAllUsers();

    public User getByNameAndPass(String name, String pass);

    User getById(Long userId);

    List<User> updateAllRankings();

    User patch(Long userId, String name, String pass, String fullName, String email);

    User deleteById(Long userId);

    User add(User user);

    int calculateUserTokens(User user, int amount);

    int calculateUserBadge(User user);
}
