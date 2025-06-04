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
import ru.ivalykhin.subscriptions.entity.Subscription;
import ru.ivalykhin.subscriptions.service.SubscriptionService;
import ru.ivalykhin.subscriptions.service.UserSubscriptionService;

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
    private UserSubscriptionService userSubscriptionService;
    @MockitoBean
    private SubscriptionService subscriptionService;

    private final String subscriptionsTopEndpoint = "/subscriptions/top";
    private static List<SubscriptionDto> subscriptionDtoList;
    private static List<Subscription> subscriptionList;

    @BeforeAll
    public static void setUp() {
        subscriptionDtoList = List.of(SubscriptionDto.builder()
                        .id(UUID.randomUUID())
                        .name("test1")
                        .description("test description1")
                        .build(),
                SubscriptionDto.builder()
                        .id(UUID.randomUUID())
                        .name("test2")
                        .description("test description2")
                        .build(),
                SubscriptionDto.builder()
                        .id(UUID.randomUUID())
                        .name("test3")
                        .description("test description3")
                        .build()
        );
        subscriptionList = subscriptionDtoList.stream()
                .map(subscriptionDto ->
                        Subscription.builder()
                                .id(subscriptionDto.getId())
                                .name(subscriptionDto.getName())
                                .description(subscriptionDto.getDescription())
                                .build())
                .toList();
    }

    @Test
    public void getTopSubscriptions_HasData_success() throws Exception {

        when(userSubscriptionService.getTopSubscriptions(3)).thenReturn(subscriptionList);
        when(subscriptionService.convertEntitiesToDto(subscriptionList)).thenReturn(subscriptionDtoList);

        mockMvc.perform(get(subscriptionsTopEndpoint)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(subscriptionDtoList))
                );
        verify(userSubscriptionService, times(1)).getTopSubscriptions(3);
    }

    @Test
    public void getTopSubscriptions_NoData_success() throws Exception {

        when(userSubscriptionService.getTopSubscriptions(3)).thenReturn(Collections.emptyList());
        when(subscriptionService.convertEntitiesToDto(subscriptionList)).thenReturn(Collections.emptyList());

        mockMvc.perform(get(subscriptionsTopEndpoint)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.emptyList()))
                );
        verify(userSubscriptionService, times(1)).getTopSubscriptions(3);
    }
}

