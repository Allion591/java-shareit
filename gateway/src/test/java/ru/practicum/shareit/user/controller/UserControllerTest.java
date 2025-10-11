package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserPatchDto;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserClient userClient;

    @Test
    void saveNewUser_withValidData_shouldReturnOk() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setName("John");
        userDto.setEmail("john@example.com");
        userDto.setLogin("john");

        when(userClient.saveNewUser(any(UserDto.class)))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());

        verify(userClient, times(1)).saveNewUser(any(UserDto.class));
    }

    @Test
    void saveNewUser_withInvalidEmail_shouldReturnBadRequest() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setName("John");
        userDto.setEmail("invalid-email"); // Invalid email
        userDto.setLogin("john");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());

        verify(userClient, never()).saveNewUser(any(UserDto.class));
    }

    @Test
    void getUserById_shouldReturnUser() throws Exception {
        Long userId = 1L;
        when(userClient.getById(userId))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isOk());

        verify(userClient, times(1)).getById(userId);
    }

    @Test
    void getAllUsers_shouldReturnUserList() throws Exception {
        when(userClient.getAllUsers())
                .thenReturn(ResponseEntity.ok(List.of()));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());

        verify(userClient, times(1)).getAllUsers();
    }

    @Test
    void updateUser_shouldReturnUpdatedUser() throws Exception {
        Long userId = 1L;
        UserPatchDto userPatchDto = new UserPatchDto();
        userPatchDto.setName(Optional.of("Updated Name"));
        userPatchDto.setEmail(Optional.of("updated@example.com"));

        when(userClient.update(eq(userId), any(UserPatchDto.class)))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(patch("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userPatchDto)))
                .andExpect(status().isOk());

        verify(userClient, times(1)).update(eq(userId), any(UserPatchDto.class));
    }

    @Test
    void deleteUserById_shouldCallDelete() throws Exception {
        Long userId = 1L;

        doNothing().when(userClient).deleteById(userId);

        mockMvc.perform(delete("/users/{userId}", userId))
                .andExpect(status().isOk());

        verify(userClient, times(1)).deleteById(userId);
    }
}