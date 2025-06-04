package ru.ivalykhin.subscriptions.service;

import ru.ivalykhin.subscriptions.dto.UserSubscriptionDto;
import ru.ivalykhin.subscriptions.entity.Subscription;

import java.util.List;
import java.util.UUID;

public interface UserSubscriptionService {
    UserSubscriptionDto createUserSubscription(UUID userId, UserSubscriptionDto userSubscriptionDto);

    List<UserSubscriptionDto> getUserSubscriptions(UUID userId);

    void deleteUserSubscription(UUID userId, UUID subscriptionId);

    List<Subscription> getTopSubscriptions(int limit);
}
