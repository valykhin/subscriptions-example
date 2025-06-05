package ru.ivalykhin.subscriptions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ivalykhin.subscriptions.entity.User;
import ru.ivalykhin.subscriptions.entity.UserSubscription;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, UUID> {
    List<UserSubscription> findAllByUser(User user);

    Optional<UserSubscription> findByUserAndSubscriptionId(User user, UUID uuid);
}
