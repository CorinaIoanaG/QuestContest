package com.QuestContest.controller.dto;

public record PatchUserRequest(String name, String pass, String fullName, String email) {
}
