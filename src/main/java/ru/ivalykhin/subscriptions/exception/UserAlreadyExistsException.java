package ru.ivalykhin.subscriptions.exception;

import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends RuntimeException implements SubscriptionsBusinessException {
    public final String errorCode = "USER_ALREADY_EXISTS";

    public UserAlreadyExistsException(String phone) {
        super("User already exist with phone number: " + phone);
    }
}
