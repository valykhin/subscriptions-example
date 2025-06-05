package ru.ivalykhin.subscriptions.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionsTopResponseDto {
    private SubscriptionDto subscription;
    private Long count;
}
