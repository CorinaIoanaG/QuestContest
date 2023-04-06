package com.QuestContest.controller;

import com.QuestContest.model.User;
import com.QuestContest.service.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("quest") // http://host:port/quest

public class QuestContestController {
    private final UserService userService;

    @GetMapping
    public List<User> getAll() {
            return userService.getAll();
        }

    @GetMapping("{id}")
    public User getById(@PathVariable Long id) {
        return userService.getById(id);
    }

//    @PatchMapping("{id}")
//    public User patch(@PathVariable int id, @RequestBody PatchUserRequest request) {
//        return imcService.patch(id, request.fullName(), request.town(), request.contact());
//    }

    @DeleteMapping("{id}")
    public User deleteById(@PathVariable Long id) {
        return userService.deleteById(id);
    }

    @PostMapping
    public User add(@RequestBody User user) {
        return userService.add(user);
    }

//    @PostMapping("{id}/datas")
//    User addDataToUser(@PathVariable int id, @RequestBody PatchUserDataRequest patchUserDataRequest) {
//        return imcService.addDataToUser(id, patchUserDataRequest);
//    }

}
