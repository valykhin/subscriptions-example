package ru.ivalykhin.subscriptions.service;

import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ivalykhin.subscriptions.dto.SubscriptionDto;
import ru.ivalykhin.subscriptions.dto.SubscriptionsTopResponseDto;
import ru.ivalykhin.subscriptions.entity.Subscription;
import ru.ivalykhin.subscriptions.exception.SubscriptionNotFoundException;
import ru.ivalykhin.subscriptions.mapper.SubscriptionMapper;
import ru.ivalykhin.subscriptions.mapper.SubscriptionsTopMapper;
import ru.ivalykhin.subscriptions.repository.SubscriptionRepository;
import ru.ivalykhin.subscriptions.repository.UserSubscriptionTopRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final UserSubscriptionTopRepository userSubscriptionTopRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final SubscriptionsTopMapper subscriptionsTopMapper;

    @Override
    public SubscriptionDto getSubscription(UUID subscriptionId) {
        Subscription subscription = getSubscriptionEntity(subscriptionId);
        return subscriptionMapper.entityToDto(subscription);
    }

    @Override
    @Transactional(readOnly = true)
    public Subscription getSubscriptionEntity(UUID subscriptionId) {
        return subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new SubscriptionNotFoundException(subscriptionId));
    }

    @Override
    public List<SubscriptionDto> convertEntitiesToDto(List<Subscription> subscriptions) {
        return subscriptions.stream().map(subscriptionMapper::entityToDto).toList();
    }

    @Override
    public List<SubscriptionsTopResponseDto> getTopSubscriptions(int limit) {

        List<Tuple> tuples = userSubscriptionTopRepository.findTopSubscriptions(Pageable.ofSize(limit));

        return subscriptionsTopMapper.fromTuples(tuples);
    }
}
