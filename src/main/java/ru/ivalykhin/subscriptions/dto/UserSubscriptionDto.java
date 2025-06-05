package ru.ivalykhin.subscriptions.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class UserSubscriptionDto {
    private UUID id;
    @JsonProperty("user_id")
    private UUID userId;
    @JsonProperty("subscription_id")
    private UUID subscriptionId;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("expired_at")
    private LocalDateTime expiredAt;
}
