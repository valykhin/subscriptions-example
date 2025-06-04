package ru.ivalykhin.subscriptions.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ivalykhin.subscriptions.dto.SubscriptionDto;
import ru.ivalykhin.subscriptions.service.SubscriptionService;
import ru.ivalykhin.subscriptions.service.UserSubscriptionService;

import java.util.List;

@Tag(name = "Subscriptions API", description = "Provides API to get top subscriptions")
@RestController
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {
    private final UserSubscriptionService userSubscriptionService;
    private final SubscriptionService subscriptionService;

    @GetMapping(value = "/top", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SubscriptionDto> getTopSubscriptions() {
        return subscriptionService.convertEntitiesToDto(
                userSubscriptionService.getTopSubscriptions(3)
        );
    }
}
