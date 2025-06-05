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
import ru.ivalykhin.subscriptions.dto.UserDto;
import ru.ivalykhin.subscriptions.exception.UserAlreadyExistsException;
import ru.ivalykhin.subscriptions.exception.UserNotFoundException;
import ru.ivalykhin.subscriptions.service.UserServiceImpl;
import ru.ivalykhin.subscriptions.util.ErrorResponseUtil;

import java.util.UUID;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UserServiceImpl userServiceImpl;

    private final String usersEndpoint = "/users";
    private final String specifiedUserEndpoint = "/users/%s";
    private UUID userId;

    @BeforeEach
    public void setUp() {
        userId = UUID.randomUUID();
    }

    @Nested
    public class createUser {
        @Test
        public void createUser_FullInfo_success() throws Exception {
            UserDto userCreateRequestDto = createUserRequestDto();
            UserDto userCreateResponseDto = createUserRequestDto();
            userCreateResponseDto.setId(UUID.randomUUID());

            when(userServiceImpl.createUser(
                    userCreateRequestDto.getName(),
                    userCreateRequestDto.getSurname(),
                    userCreateRequestDto.getPhone())).thenReturn(userCreateResponseDto);

            mockMvc.perform(post(usersEndpoint)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content((objectMapper.writeValueAsString(userCreateRequestDto)))
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().json("""
                                    {
                                    "id": "%s",
                                    "name": "%s",
                                    "surname": "%s",
                                    "phone": %s
                                    }
                                    """.formatted(
                                    userCreateResponseDto.getId(),
                                    userCreateResponseDto.getName(),
                                    userCreateResponseDto.getSurname(),
                                    userCreateResponseDto.getPhone()
                            )
                    ));
            verifyCreateUserServiceCall(userCreateRequestDto);
        }

        @Test
        public void createUser_UserAlreadyExists_thenError() throws Exception {
            UserDto userCreateRequestDto = createUserRequestDto();
            UserAlreadyExistsException exception =
                    new UserAlreadyExistsException(userCreateRequestDto.getPhone());

            when(userServiceImpl.createUser(
                    userCreateRequestDto.getName(),
                    userCreateRequestDto.getSurname(),
                    userCreateRequestDto.getPhone())).thenThrow(exception);

            mockMvc.perform(post(usersEndpoint)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content((objectMapper.writeValueAsString(userCreateRequestDto)))
                    )
                    .andExpect(status().isConflict())
                    .andExpect(content().json(
                            ErrorResponseUtil.getErrorMessage(exception))
                    );
            verifyCreateUserServiceCall(userCreateRequestDto);
        }

        @Test
        public void createUser_NoName_thenError() throws Exception {
            UserDto userCreateRequestDto = createUserRequestDto();
            userCreateRequestDto.setName(null);

            mockMvc.perform(post(usersEndpoint)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content((objectMapper.writeValueAsString(userCreateRequestDto)))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(
                            allOf(stringContainsInOrder(
                                    "VALIDATION_ERROR",
                                    "Field error in object 'userDto' on field 'name': rejected value [null]"
                            )))
                    );
        }

        @Test
        public void createUser_NoSurname_thenError() throws Exception {
            UserDto userCreateRequestDto = createUserRequestDto();
            userCreateRequestDto.setSurname(null);

            mockMvc.perform(post(usersEndpoint)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content((objectMapper.writeValueAsString(userCreateRequestDto)))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(
                            allOf(stringContainsInOrder(
                                    "VALIDATION_ERROR",
                                    "Field error in object 'userDto' on field 'surname': rejected value [null]"
                            )))
                    );
        }

        @Test
        public void createUser_NoPhone_thenError() throws Exception {
            UserDto userCreateRequestDto = createUserRequestDto();
            userCreateRequestDto.setPhone(null);

            mockMvc.perform(post(usersEndpoint)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content((objectMapper.writeValueAsString(userCreateRequestDto)))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(
                            allOf(stringContainsInOrder(
                                    "VALIDATION_ERROR",
                                    "Field error in object 'userDto' on field 'phone': rejected value [null]"
                            )))
                    );
        }

        private void verifyCreateUserServiceCall(UserDto userDto) {
            verify(userServiceImpl, times(1))
                    .createUser(
                            userDto.getName(),
                            userDto.getSurname(),
                            userDto.getPhone());
        }
    }

    @Nested
    public class updateUser {
        @Test
        public void updateUser_FullInfo_success() throws Exception {
            UserDto userDto = createUserDto(userId);
            userDto.setName("tested");

            when(userServiceImpl.updateUser(
                    userDto.getId(),
                    userDto)).thenReturn(userDto);

            mockMvc.perform(put(specifiedUserEndpoint.formatted(userId))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content((objectMapper.writeValueAsString(userDto)))
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().json("""
                                    {
                                    "id": "%s",
                                    "name": "%s",
                                    "surname": "%s",
                                    "phone": %s
                                    }
                                    """.formatted(
                                    userDto.getId(),
                                    userDto.getName(),
                                    userDto.getSurname(),
                                    userDto.getPhone()
                            )
                    ));
            verifyUpdateUserServiceCall(userDto);
        }

        @Test
        public void updateUser_NoName_error() throws Exception {
            UserDto userDto = createUserDto(userId);
            userDto.setName(null);

            mockMvc.perform(post(usersEndpoint)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content((objectMapper.writeValueAsString(userDto)))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(
                            allOf(stringContainsInOrder(
                                    "VALIDATION_ERROR",
                                    "Field error in object 'userDto' on field 'name': rejected value [null]"
                            )))
                    );
        }

        @Test
        public void updateUser_NoUser_error() throws Exception {
            UserDto userDto = createUserDto(userId);
            userDto.setName("tested");

            UserNotFoundException exception = new UserNotFoundException(userId);

            when(userServiceImpl.updateUser(
                    userDto.getId(),
                    userDto)).thenThrow(exception);

            mockMvc.perform(put(specifiedUserEndpoint.formatted(userId))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content((objectMapper.writeValueAsString(userDto)))
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(
                            ErrorResponseUtil.getErrorMessage(exception))
                    );
            verifyUpdateUserServiceCall(userDto);
        }

        private void verifyUpdateUserServiceCall(UserDto userDto) {
            verify(userServiceImpl, times(1))
                    .updateUser(
                            userDto.getId(),
                            userDto);
        }
    }

    @Nested
    public class getUser {
        @Test
        public void getUser_UserExists_success() throws Exception {
            UserDto userDto = createUserDto(userId);

            when(userServiceImpl.getUser(
                    userDto.getId())).thenReturn(userDto);

            mockMvc.perform(get(specifiedUserEndpoint.formatted(userId))
                            .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().json("""
                                    {
                                    "id": "%s",
                                    "name": "%s",
                                    "surname": "%s",
                                    "phone": %s
                                    }
                                    """.formatted(
                                    userDto.getId(),
                                    userDto.getName(),
                                    userDto.getSurname(),
                                    userDto.getPhone()
                            )
                    ));
            verifyGetUserServiceCall(userDto);
        }

        @Test
        public void getUser_UserNotExists_success() throws Exception {
            UserDto userDto = createUserDto(userId);
            UserNotFoundException exception = new UserNotFoundException(userId);

            when(userServiceImpl.getUser(
                    userDto.getId())).thenThrow(exception);

            mockMvc.perform(get(specifiedUserEndpoint.formatted(userId))
                            .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(
                            ErrorResponseUtil.getErrorMessage(exception))
                    );
            verifyGetUserServiceCall(userDto);
        }

        private void verifyGetUserServiceCall(UserDto userDto) {
            verify(userServiceImpl, times(1))
                    .getUser(userDto.getId());
        }
    }

    @Nested
    public class deleteUser {
        @Test
        public void deleteUser_UserExists_success() throws Exception {
            mockMvc.perform(delete(specifiedUserEndpoint.formatted(userId))
                            .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().string(""));
            verifyDeleteUserServiceCall();
        }

        @Test
        public void deleteUser_UserNotExists_success() throws Exception {
            UserNotFoundException exception = new UserNotFoundException(userId);

            doThrow(exception).when(userServiceImpl).deleteUser(userId);

            mockMvc.perform(delete(specifiedUserEndpoint.formatted(userId))
                            .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(ErrorResponseUtil.getErrorMessage(exception)));
            verifyDeleteUserServiceCall();
        }

        private void verifyDeleteUserServiceCall() {
            verify(userServiceImpl, times(1))
                    .deleteUser(userId);
        }
    }


    private UserDto createUserRequestDto() {
        return UserDto.builder()
                .name("test")
                .surname("tester")
                .phone("7-999-999-0000")
                .build();
    }

    private UserDto createUserDto(UUID uuid) {
        return UserDto.builder()
                .id(uuid)
                .name("test")
                .surname("tester")
                .phone("7-999-999-0000")
                .build();
    }
}
