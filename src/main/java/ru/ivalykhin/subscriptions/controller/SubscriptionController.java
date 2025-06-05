package ru.ivalykhin.subscriptions.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ivalykhin.subscriptions.dto.SubscriptionsTopResponseDto;
import ru.ivalykhin.subscriptions.service.SubscriptionService;

import java.util.List;

@Tag(name = "Subscriptions API", description = "Provides API to get top subscriptions")
@RestController
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @GetMapping(value = "/top", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SubscriptionsTopResponseDto> getTopSubscriptions() {
        return subscriptionService.getTopSubscriptions(3);
    }
}
