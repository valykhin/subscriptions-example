package ru.ivalykhin.subscriptions.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "user_subscriptions")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSubscription {
    @Id
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne
    @JoinColumn(name = "subscription_id", nullable = false)
    private Subscription subscription;

    @CreatedDate
    private LocalDateTime createdAt;
}
