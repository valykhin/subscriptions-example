package ru.ivalykhin.subscriptions.service;

import ru.ivalykhin.subscriptions.dto.SubscriptionDto;
import ru.ivalykhin.subscriptions.entity.Subscription;

import java.util.List;
import java.util.UUID;

public interface SubscriptionService {
    SubscriptionDto getSubscription(UUID subscriptionId);

    Subscription getSubscriptionEntity(UUID subscriptionId);

    List<SubscriptionDto> convertEntitiesToDto(List<Subscription> subscriptions);
}
