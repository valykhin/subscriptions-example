package ru.ivalykhin.subscriptions.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.ivalykhin.subscriptions.dto.UserDto;
import ru.ivalykhin.subscriptions.service.UserService;

import java.util.UUID;

@Tag(name = "Users API", description = "Provides API for user management")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto create(@RequestBody @Valid UserDto request) {
        return userService.createUser(
                request.getName(),
                request.getSurname(),
                request.getPhone());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto getUser(@PathVariable UUID id) {
        return userService.getUser(id);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto updateUser(@PathVariable UUID id,
                              @RequestBody @Valid UserDto request) {
        return userService.updateUser(id, request);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
    }
}
