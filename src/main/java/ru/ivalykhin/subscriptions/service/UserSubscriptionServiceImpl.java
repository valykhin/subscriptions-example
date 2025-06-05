package ru.ivalykhin.subscriptions.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.ivalykhin.subscriptions.dto.UserSubscriptionDto;
import ru.ivalykhin.subscriptions.entity.Subscription;
import ru.ivalykhin.subscriptions.entity.User;
import ru.ivalykhin.subscriptions.entity.UserSubscription;
import ru.ivalykhin.subscriptions.exception.UserSubscriptionNotFoundException;
import ru.ivalykhin.subscriptions.mapper.UserSubscriptionMapper;
import ru.ivalykhin.subscriptions.repository.UserSubscriptionRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserSubscriptionServiceImpl implements UserSubscriptionService {
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final UserService userService;
    private final SubscriptionService subscriptionService;
    private final UserSubscriptionMapper userSubscriptionMapper;

    @Override
    public UserSubscriptionDto createUserSubscription(UUID userId, UserSubscriptionDto userSubscriptionDto) {
        User user = userService.getUserEntity(userId);
        Subscription subscription =
                subscriptionService.getSubscriptionEntity(userSubscriptionDto.getSubscriptionId());

        UserSubscription userSubscription = UserSubscription.builder()
                .user(user)
                .subscription(subscription)
                .build();
        userSubscription = userSubscriptionRepository.save(userSubscription);

        return userSubscriptionMapper.entityToDto(userSubscription);
    }

    @Override
    public List<UserSubscriptionDto> getUserSubscriptions(UUID userId) {
        User user = userService.getUserEntity(userId);

        return userSubscriptionRepository.findAllByUser(user)
                .stream()
                .map(userSubscriptionMapper::entityToDto)
                .toList();
    }

    @Override
    public void deleteUserSubscription(UUID userId, UUID subscriptionId) {
        User user = userService.getUserEntity(userId);

        UserSubscription userSubscription =
                userSubscriptionRepository.findByUserAndSubscriptionId(user, subscriptionId)
                        .orElseThrow(() -> new UserSubscriptionNotFoundException(userId, subscriptionId));

        userSubscriptionRepository.delete(userSubscription);
    }

    @Override
    public List<Subscription> getTopSubscriptions(int limit) {
        return userSubscriptionRepository.findTopSubscriptions(Pageable.ofSize(limit));
    }
}
