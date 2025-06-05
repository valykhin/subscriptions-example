package ru.ivalykhin.subscriptions.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UserSubscriptionNotFoundException extends RuntimeException implements SubscriptionsBusinessException {
    public final String errorCode = "USER_SUBSCRIPTION_NOT_FOUND";

    public UserSubscriptionNotFoundException(UUID userId, UUID subscriptionId) {
        super("Subscription %s for user %s not found".formatted(subscriptionId, userId));
    }
}
