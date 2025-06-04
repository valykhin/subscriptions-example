package ru.ivalykhin.subscriptions.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.ivalykhin.subscriptions.dto.UserDto;
import ru.ivalykhin.subscriptions.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto entityToDto(User user);
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User updateEntityFromDto(UserDto userDto, @MappingTarget User user);
}
