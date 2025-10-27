package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.EmailAlreadyExistsException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserPatchDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void create_ShouldCreateUser() {
        UserDto userDto = new UserDto();
        userDto.setName("Test User");
        userDto.setEmail("test@mail.com");

        User user = new User();
        user.setName("Test User");
        user.setEmail("test@mail.com");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("Test User");
        savedUser.setEmail("test@mail.com");

        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(1L);
        responseDto.setName("Test User");
        responseDto.setEmail("test@mail.com");

        when(userRepository.existsByEmail("test@mail.com")).thenReturn(false);
        when(userMapper.toUser(any(UserDto.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userMapper.toResponseDto(any(User.class))).thenReturn(responseDto);

        UserResponseDto result = userService.create(userDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test User", result.getName());
        verify(userRepository).existsByEmail("test@mail.com");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void create_WhenEmailExists_ShouldThrowException() {
        UserDto userDto = new UserDto();
        userDto.setName("Test User");
        userDto.setEmail("existing@mail.com");

        when(userRepository.existsByEmail("existing@mail.com")).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.create(userDto));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getById_ShouldReturnUser() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setName("Test User");

        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(userId);
        responseDto.setName("Test User");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toResponseDto(any(User.class))).thenReturn(responseDto);
        UserResponseDto result = userService.getById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("Test User", result.getName());
        verify(userRepository).findById(userId);
    }

    @Test
    void getById_WhenUserNotExists_ShouldThrowException() {
        Long userId = 999L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getById(userId));
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        User user1 = new User();
        user1.setId(1L);
        user1.setName("User 1");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("User 2");

        UserResponseDto responseDto1 = new UserResponseDto();
        responseDto1.setId(1L);
        responseDto1.setName("User 1");

        UserResponseDto responseDto2 = new UserResponseDto();
        responseDto2.setId(2L);
        responseDto2.setName("User 2");

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));
        when(userMapper.toResponseDto(user1)).thenReturn(responseDto1);
        when(userMapper.toResponseDto(user2)).thenReturn(responseDto2);

        List<UserResponseDto> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("User 1", result.get(0).getName());
        assertEquals("User 2", result.get(1).getName());
        verify(userRepository).findAll();
    }

    @Test
    void update_ShouldUpdateUser() {
        Long userId = 1L;
        UserPatchDto patchDto = new UserPatchDto();
        patchDto.setName(Optional.of("Updated User"));
        patchDto.setEmail(Optional.of("updated@mail.com"));

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setName("Old User");
        existingUser.setEmail("old@mail.com");

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setName("Updated User");
        updatedUser.setEmail("updated@mail.com");

        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(userId);
        responseDto.setName("Updated User");
        responseDto.setEmail("updated@mail.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmailAndIdNot("updated@mail.com", userId)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(userMapper.toResponseDto(any(User.class))).thenReturn(responseDto);

        UserResponseDto result = userService.update(patchDto, userId);

        assertNotNull(result);
        assertEquals("Updated User", result.getName());
        assertEquals("updated@mail.com", result.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void update_WhenEmailExistsForOtherUser_ShouldThrowException() {
        Long userId = 1L;
        UserPatchDto patchDto = new UserPatchDto();
        patchDto.setEmail(Optional.of("existing@mail.com"));

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setEmail("old@mail.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmailAndIdNot("existing@mail.com", userId)).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.update(patchDto, userId));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void update_WhenOnlyNameProvided_ShouldUpdateOnlyName() {
        Long userId = 1L;
        UserPatchDto patchDto = new UserPatchDto();
        patchDto.setName(Optional.of("Updated User"));

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setName("Old User");
        existingUser.setEmail("old@mail.com");

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setName("Updated User");
        updatedUser.setEmail("old@mail.com");

        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(userId);
        responseDto.setName("Updated User");
        responseDto.setEmail("old@mail.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(userMapper.toResponseDto(any(User.class))).thenReturn(responseDto);

        UserResponseDto result = userService.update(patchDto, userId);

        assertNotNull(result);
        assertEquals("Updated User", result.getName());
        assertEquals("old@mail.com", result.getEmail());
    }

    @Test
    void deleteById_ShouldDeleteUser() {
        Long userId = 1L;

        doNothing().when(userRepository).deleteById(userId);

        userService.deleteById(userId);

        verify(userRepository).deleteById(userId);
    }

    @Test
    void findById_ShouldReturnUser() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setName("Test User");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.findById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("Test User", result.getName());
    }

    @Test
    void findById_WhenUserNotExists_ShouldThrowException() {
        Long userId = 999L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.findById(userId));
    }
}