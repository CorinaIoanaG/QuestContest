package com.QuestContest.controller;

import com.QuestContest.controller.dto.PostQuestRequest;
import com.QuestContest.controller.dto.PatchUserRequest;
import com.QuestContest.model.User;
import com.QuestContest.service.QuestService;
import com.QuestContest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("quest") // http://host:port/quest

public class QuestContestController {
    private final UserService userService;
    private final QuestService questService;

    @GetMapping
    public List<User> getUsers() {
        return userService.getAll();
    }

    @GetMapping(path= "/log")
    public User getByNameAndPass(@RequestParam String name, @RequestParam String pass){
        return userService.getByNameAndPass(name, pass);
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
    User addQuest(@PathVariable Long id, @RequestBody PostQuestRequest postQuestRequest) {
        return questService.addQuestfromUser(id, postQuestRequest);
    }

}
