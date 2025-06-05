package ru.ivalykhin.subscriptions.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.ivalykhin.subscriptions.dto.SubscriptionDto;
import ru.ivalykhin.subscriptions.dto.SubscriptionsTopResponseDto;
import ru.ivalykhin.subscriptions.service.SubscriptionService;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SubscriptionController.class)
public class SubscriptionControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private SubscriptionService subscriptionService;

    private final String subscriptionsTopEndpoint = "/subscriptions/top";
    private static List<SubscriptionsTopResponseDto> subscriptionsTopResponseDtoList;

    @BeforeAll
    public static void setUp() {
        subscriptionsTopResponseDtoList = List.of(
                SubscriptionsTopResponseDto.builder()
                        .subscription(SubscriptionDto.builder()
                                .id(UUID.randomUUID())
                                .name("test1")
                                .description("test description1")
                                .build())
                        .count(6L)
                        .build(),
                SubscriptionsTopResponseDto.builder()
                        .subscription(SubscriptionDto.builder()
                                .id(UUID.randomUUID())
                                .name("test2")
                                .description("test description2")
                                .build())
                        .count(4L)
                        .build(),
                SubscriptionsTopResponseDto.builder()
                        .subscription(SubscriptionDto.builder()
                                .id(UUID.randomUUID())
                                .name("test3")
                                .description("test description3")
                                .build())
                        .count(2L)
                        .build()
        );
    }

    @Test
    public void getTopSubscriptions_HasData_success() throws Exception {

        when(subscriptionService.getTopSubscriptions(3)).thenReturn(subscriptionsTopResponseDtoList);

        mockMvc.perform(get(subscriptionsTopEndpoint)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(subscriptionsTopResponseDtoList))
                );
        verify(subscriptionService, times(1)).getTopSubscriptions(3);
    }

    @Test
    public void getTopSubscriptions_NoData_success() throws Exception {

        when(subscriptionService.getTopSubscriptions(3)).thenReturn(Collections.emptyList());

        mockMvc.perform(get(subscriptionsTopEndpoint)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.emptyList()))
                );
        verify(subscriptionService, times(1)).getTopSubscriptions(3);
    }
}

