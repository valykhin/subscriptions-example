package ru.ivalykhin.subscriptions.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ivalykhin.subscriptions.dto.UserDto;
import ru.ivalykhin.subscriptions.entity.User;
import ru.ivalykhin.subscriptions.exception.UserAlreadyExistsException;
import ru.ivalykhin.subscriptions.exception.UserNotFoundException;
import ru.ivalykhin.subscriptions.mapper.UserMapper;
import ru.ivalykhin.subscriptions.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto createUser(String name, String surname, String phone) {
        Optional<User> existingUser = userRepository.findByPhone(phone);

        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException(existingUser.get().getPhone());
        }

        User user = User.builder()
                .name(name)
                .surname(surname)
                .phone(phone)
                .build();

        userRepository.save(user);

        return userMapper.entityToDto(user);
    }

    public UserDto getUser(UUID userId) {
        User user = getUserEntity(userId);
        return userMapper.entityToDto(user);
    }

    @Transactional(readOnly = true)
    public User getUserEntity(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Transactional
    public UserDto updateUser(UUID userId, UserDto userDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        user = userMapper.updateEntityFromDto(userDto, user);
        user = userRepository.save(user);

        return userMapper.entityToDto(user);
    }

    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        userRepository.delete(user);
    }
}
