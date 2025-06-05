package ru.ivalykhin.subscriptions.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.ivalykhin.subscriptions.entity.Subscription;
import ru.ivalykhin.subscriptions.entity.User;
import ru.ivalykhin.subscriptions.entity.UserSubscription;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, UUID> {
    List<UserSubscription> findAllByUser(User user);

    Optional<UserSubscription> findByUserAndSubscriptionId(User user, UUID uuid);

    @Query("""
                SELECT us.subscription
                FROM UserSubscription us
                GROUP BY us.subscription
                ORDER BY COUNT(us.user.id) DESC
            """)
    List<Subscription> findTopSubscriptions(Pageable pageable);
}
