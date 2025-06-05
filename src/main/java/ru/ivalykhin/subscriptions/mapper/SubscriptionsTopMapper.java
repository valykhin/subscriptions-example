package ru.ivalykhin.subscriptions.mapper;

import jakarta.persistence.Tuple;
import org.mapstruct.Mapper;
import ru.ivalykhin.subscriptions.dto.SubscriptionDto;
import ru.ivalykhin.subscriptions.dto.SubscriptionsTopResponseDto;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface SubscriptionsTopMapper {

    default SubscriptionsTopResponseDto fromTuple(Tuple tuple) {
        return SubscriptionsTopResponseDto.builder()
                .subscription(SubscriptionDto.builder()
                        .id(tuple.get("subscriptionId", UUID.class))
                        .name(tuple.get("name", String.class))
                        .description(tuple.get("description", String.class))
                        .build())
                .count(tuple.get("userCount", Long.class))
                .build();
    }

    List<SubscriptionsTopResponseDto> fromTuples(List<Tuple> tuples);
}
