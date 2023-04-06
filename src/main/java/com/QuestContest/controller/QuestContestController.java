package com.QuestContest.controller;

import com.QuestContest.controller.dto.PatchQuestRequest;
import com.QuestContest.controller.dto.PatchUserRequest;
import com.QuestContest.model.User;
import com.QuestContest.service.UserService;
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

   @PatchMapping("{id}")
    public User patch(@PathVariable Long id, @RequestBody PatchUserRequest request) {
        return userService.patch(id, request.name(), request.pass(), request.fullName(), request.email());
    }

    @DeleteMapping("{id}")
    public User deleteById(@PathVariable Long id) {
        return userService.deleteById(id);
    }

    @PostMapping
    public User add(@RequestBody User user) {
        return userService.add(user);
    }

    @PostMapping("{id}/quest")
    User addQuest(@PathVariable Long id, @RequestBody PatchQuestRequest patchQuestRequest) {
        return userService.addQuestfromUser(id, patchQuestRequest);
    }

}
