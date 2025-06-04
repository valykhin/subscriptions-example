package ru.ivalykhin.subscriptions.service;

import ru.ivalykhin.subscriptions.dto.UserDto;
import ru.ivalykhin.subscriptions.entity.User;

import java.util.UUID;

public interface UserService {
    UserDto createUser(String name, String surname, String phone);

    UserDto getUser(UUID userId);

    User getUserEntity(UUID userId);

    UserDto updateUser(UUID userId, UserDto userDto);

    void deleteUser(UUID userId);
}
