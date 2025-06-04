package ru.ivalykhin.subscriptions.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.ivalykhin.subscriptions.dto.UserSubscriptionDto;
import ru.ivalykhin.subscriptions.service.UserSubscriptionService;

import java.util.List;
import java.util.UUID;

@Tag(name = "User Subscriptions API", description = "Provides API for users subscriptions management")
@RestController
@RequestMapping("/users/{id}/subscriptions")
@RequiredArgsConstructor
public class UserSubscriptionController {
    private final UserSubscriptionService userSubscriptionService;


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public UserSubscriptionDto createUserSubscription(@PathVariable UUID userId,
                                                      @RequestBody @Valid UserSubscriptionDto request) {
        return userSubscriptionService.createUserSubscription(userId, request);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserSubscriptionDto> getUserSubscriptions(@PathVariable UUID userId) {
        return userSubscriptionService.getUserSubscriptions(userId);
    }

    @DeleteMapping
    public void deleteUserSubscription(@PathVariable UUID userId,
                                       @PathVariable UUID userSubscriptionId) {
        userSubscriptionService.deleteUserSubscription(userId, userSubscriptionId);
    }
}
