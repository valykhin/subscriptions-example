package ru.ivalykhin.subscriptions.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UserNotFoundException extends RuntimeException implements SubscriptionsBusinessException {
    public final String errorCode = "USER_NOT_FOUND";

    public UserNotFoundException(UUID userId) {
        super("User not found: " + userId);
    }
}
