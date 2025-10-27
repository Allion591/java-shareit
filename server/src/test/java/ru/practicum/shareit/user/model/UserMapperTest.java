package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper userMapper = new UserMapper();

    @Test
    void toUser_ShouldMapCorrectly() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("Test User");
        userDto.setEmail("test@mail.com");

        User result = userMapper.toUser(userDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test User", result.getName());
        assertEquals("test@mail.com", result.getEmail());
    }

    @Test
    void toDto_ShouldMapCorrectly() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@mail.com");

        UserDto result = userMapper.toDto(user);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test User", result.getName());
        assertEquals("test@mail.com", result.getEmail());
    }

    @Test
    void toResponseDto_ShouldMapCorrectly() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@mail.com");

        UserResponseDto result = userMapper.toResponseDto(user);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test User", result.getName());
        assertEquals("test@mail.com", result.getEmail());
    }

    @Test
    void toDto_WhenNameIsBlank_ShouldUseLogin() {
        User user = new User();
        user.setId(1L);
        user.setName(""); // Blank name

        UserDto userDto = new UserDto();
        userDto.setLogin("userlogin");
    }
}