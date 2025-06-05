package ru.ivalykhin.subscriptions.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.ivalykhin.subscriptions.dto.UserSubscriptionDto;
import ru.ivalykhin.subscriptions.exception.SubscriptionNotFoundException;
import ru.ivalykhin.subscriptions.exception.UserNotFoundException;
import ru.ivalykhin.subscriptions.exception.UserSubscriptionNotFoundException;
import ru.ivalykhin.subscriptions.service.SubscriptionService;
import ru.ivalykhin.subscriptions.service.UserSubscriptionService;
import ru.ivalykhin.subscriptions.util.ErrorResponseUtil;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserSubscriptionController.class)
public class UserSubscriptionControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UserSubscriptionService userSubscriptionService;
    @MockitoBean
    private SubscriptionService subscriptionService;

    private final String userSubscriptionEndpoint = "/users/%s/subscriptions";
    UUID userId;
    UUID subscriptionId;
    private UserSubscriptionDto userSubscriptionDto;
    private UserSubscriptionDto userSubscriptionResponseDto;

    @BeforeEach
    public void setUp() {
        userId = UUID.randomUUID();
        subscriptionId = UUID.randomUUID();
        userSubscriptionDto = createUserSubscriptionDto(userId, subscriptionId);
        userSubscriptionResponseDto = createUserSubscriptionDto(userId, subscriptionId);
        userSubscriptionResponseDto.setId(UUID.randomUUID());
    }

    @Nested
    public class createUserSubscription {
        @Test
        public void createUserSubscription_FullInfo_success() throws Exception {
            when(userSubscriptionService.createUserSubscription(userId, userSubscriptionDto))
                    .thenReturn(userSubscriptionResponseDto);

            mockMvc.perform(post(userSubscriptionEndpoint.formatted(userId))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content((objectMapper.writeValueAsString(userSubscriptionDto)))
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().json("""
                            {
                            "id": "%s",
                            "user_id": "%s",
                            "subscription_id": "%s"
                            }
                            """.formatted(
                            userSubscriptionResponseDto.getId(),
                            userSubscriptionResponseDto.getUserId(),
                            userSubscriptionResponseDto.getSubscriptionId())
                    ));
            verify(userSubscriptionService, times(1))
                    .createUserSubscription(userId, userSubscriptionDto);
        }

        @Test
        public void createUserSubscription_NotExistingUser_thenError() throws Exception {
            UserNotFoundException exception = new UserNotFoundException(userId);
            when(userSubscriptionService.createUserSubscription(userId, userSubscriptionDto))
                    .thenThrow(exception);

            mockMvc.perform(post(userSubscriptionEndpoint.formatted(userId))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content((objectMapper.writeValueAsString(userSubscriptionDto)))
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(ErrorResponseUtil.getErrorMessage(exception)));
            verify(userSubscriptionService, times(1))
                    .createUserSubscription(userId, userSubscriptionDto);
        }

        @Test
        public void createUserSubscription_NotExistingSubscription_thenError() throws Exception {
            SubscriptionNotFoundException exception = new SubscriptionNotFoundException(subscriptionId);
            when(userSubscriptionService.createUserSubscription(userId, userSubscriptionDto))
                    .thenThrow(exception);

            mockMvc.perform(post(userSubscriptionEndpoint.formatted(userId))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content((objectMapper.writeValueAsString(userSubscriptionDto)))
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(ErrorResponseUtil.getErrorMessage(exception)));
            verify(userSubscriptionService, times(1))
                    .createUserSubscription(userId, userSubscriptionDto);
        }
    }

    @Nested
    public class getUserSubscription {
        @Test
        public void getUserSubscription_UserSubscriptionExists_success() throws Exception {
            when(userSubscriptionService.getUserSubscriptions(userId))
                    .thenReturn(List.of(userSubscriptionResponseDto));

            mockMvc.perform(get(userSubscriptionEndpoint.formatted(userId))
                            .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().json("""
                                    [{
                                        "id": "%s",
                                        "user_id": "%s",
                                        "subscription_id": "%s"
                                    }]
                                    """.formatted(
                                    userSubscriptionResponseDto.getId(),
                                    userSubscriptionResponseDto.getUserId(),
                                    userSubscriptionResponseDto.getSubscriptionId()
                            )
                    ));
            verify(userSubscriptionService, times(1))
                    .getUserSubscriptions(userId);
        }

        @Test
        public void getUserSubscription_UserSubscriptionNotExists_success() throws Exception {
            when(userSubscriptionService.getUserSubscriptions(userId))
                    .thenReturn(Collections.emptyList());

            mockMvc.perform(get(userSubscriptionEndpoint.formatted(userId))
                            .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().json("""
                            []
                            """
                    ));
            verify(userSubscriptionService, times(1))
                    .getUserSubscriptions(userId);
        }
    }

    @Nested
    public class deleteUserSubscription {
        @Test
        public void deleteUserSubscription_UserSubscriptionExists_success() throws Exception {
            UUID userSubscriptionId = UUID.randomUUID();

            mockMvc.perform(delete((userSubscriptionEndpoint + "/%s").formatted(userId, userSubscriptionId))
                            .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().string(""));
            verify(userSubscriptionService, times(1))
                    .deleteUserSubscription(userId, userSubscriptionId);
        }

        @Test
        public void deleteUserSubscription_UserSubscriptionNotExists_thenError() throws Exception {
            UserSubscriptionNotFoundException exception =
                    new UserSubscriptionNotFoundException(userId, subscriptionId);

            doThrow(exception).when(userSubscriptionService).deleteUserSubscription(userId, subscriptionId);

            mockMvc.perform(delete((userSubscriptionEndpoint + "/%s").formatted(userId, subscriptionId))
                            .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(ErrorResponseUtil.getErrorMessage(exception)));
            verify(userSubscriptionService, times(1))
                    .deleteUserSubscription(userId, subscriptionId);
        }
    }

    UserSubscriptionDto createUserSubscriptionDto(UUID userId, UUID subscriptionId) {
        return UserSubscriptionDto.builder()
                .userId(userId)
                .subscriptionId(subscriptionId)
                .build();
    }

}
