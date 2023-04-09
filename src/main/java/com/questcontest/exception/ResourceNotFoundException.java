package com.questcontest.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {
    private final Long entityId;

    public ResourceNotFoundException(String message, Long entityId) {
        super(message);
        this.entityId = entityId;
    }
}
