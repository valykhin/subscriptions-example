package ru.ivalykhin.subscriptions.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.ivalykhin.subscriptions.dto.SubscriptionDto;
import ru.ivalykhin.subscriptions.entity.Subscription;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    SubscriptionDto entityToDto(Subscription subscription);
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Subscription updateEntityFromDto(SubscriptionDto subscriptionDto, @MappingTarget Subscription subscription);
}
