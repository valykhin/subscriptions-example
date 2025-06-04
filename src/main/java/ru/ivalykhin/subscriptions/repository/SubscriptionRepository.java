package ru.ivalykhin.subscriptions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ivalykhin.subscriptions.entity.Subscription;

import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
}
