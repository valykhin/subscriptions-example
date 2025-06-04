package ru.ivalykhin.subscriptions.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class SubscriptionNotFoundException extends RuntimeException implements SubscriptionsBusinessException {
    public final String errorCode = "SUBSCRIPTION_NOT_FOUND";

    public SubscriptionNotFoundException(UUID userId) {
        super("Subscription not found: " + userId);
    }
}
