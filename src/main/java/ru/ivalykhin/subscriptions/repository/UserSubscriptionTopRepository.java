package ru.ivalykhin.subscriptions.repository;

import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.ivalykhin.subscriptions.entity.UserSubscription;

import java.util.List;
import java.util.UUID;

public interface UserSubscriptionTopRepository extends JpaRepository<UserSubscription, UUID> {
    @Query("""
                SELECT s.id AS subscriptionId, s.name AS name, s.description AS description, COUNT(us.user.id) AS userCount
                FROM UserSubscription us
                JOIN us.subscription s
                GROUP BY s.id, s.name, s.description
                ORDER BY COUNT(us.user.id) DESC
            """)
    List<Tuple> findTopSubscriptions(Pageable pageable);
}
