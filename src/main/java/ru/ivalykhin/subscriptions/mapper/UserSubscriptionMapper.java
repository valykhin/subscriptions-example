package ru.ivalykhin.subscriptions.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.ivalykhin.subscriptions.dto.UserSubscriptionDto;
import ru.ivalykhin.subscriptions.entity.UserSubscription;

@Mapper(componentModel = "spring")
public interface UserSubscriptionMapper {
    @Mapping(target = "userId", source = "userSubscription.user.id")
    @Mapping(target = "subscriptionId", source = "userSubscription.subscription.id")
    UserSubscriptionDto entityToDto(UserSubscription userSubscription);
}
