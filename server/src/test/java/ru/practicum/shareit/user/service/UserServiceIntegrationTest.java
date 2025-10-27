package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.EmailAlreadyExistsException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserPatchDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        testUser = new User();
        testUser.setName("Test User");
        testUser.setEmail("test@mail.com");
        testUser = userRepository.save(testUser);
    }

    @Test
    void create_IntegrationTest() {
        UserDto userDto = new UserDto();
        userDto.setName("New User");
        userDto.setEmail("newuser@mail.com");

        UserResponseDto result = userService.create(userDto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("New User", result.getName());
        assertEquals("newuser@mail.com", result.getEmail());

        Optional<User> savedUser = userRepository.findById(result.getId());
        assertTrue(savedUser.isPresent());
        assertEquals("New User", savedUser.get().getName());
    }

    @Test
    void create_WhenEmailExists_ShouldThrowException() {
        UserDto userDto = new UserDto();
        userDto.setName("Another User");
        userDto.setEmail("test@mail.com"); // Same email as existing user

        assertThrows(EmailAlreadyExistsException.class, () -> userService.create(userDto));
    }

    @Test
    void getById_IntegrationTest() {
        UserResponseDto result = userService.getById(testUser.getId());

        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals("Test User", result.getName());
        assertEquals("test@mail.com", result.getEmail());
    }

    @Test
    void getById_WhenUserNotExists_ShouldThrowException() {
        assertThrows(NotFoundException.class, () -> userService.getById(999L));
    }

    @Test
    void getAllUsers_IntegrationTest() {
        User secondUser = new User();
        secondUser.setName("Second User");
        secondUser.setEmail("second@mail.com");
        userRepository.save(secondUser);

        List<UserResponseDto> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(u -> u.getName().equals("Test User")));
        assertTrue(result.stream().anyMatch(u -> u.getName().equals("Second User")));
    }

    @Test
    void update_IntegrationTest() {
        UserPatchDto patchDto = new UserPatchDto();
        patchDto.setName(Optional.of("Updated User"));
        patchDto.setEmail(Optional.of("updated@mail.com"));

        UserResponseDto result = userService.update(patchDto, testUser.getId());

        assertNotNull(result);
        assertEquals("Updated User", result.getName());
        assertEquals("updated@mail.com", result.getEmail());

        Optional<User> updatedUser = userRepository.findById(testUser.getId());
        assertTrue(updatedUser.isPresent());
        assertEquals("Updated User", updatedUser.get().getName());
    }

    @Test
    void update_WhenEmailExistsForOtherUser_ShouldThrowException() {
        User secondUser = new User();
        secondUser.setName("Second User");
        secondUser.setEmail("second@mail.com");
        userRepository.save(secondUser);

        UserPatchDto patchDto = new UserPatchDto();
        patchDto.setEmail(Optional.of("second@mail.com"));

        assertThrows(EmailAlreadyExistsException.class, () -> userService.update(patchDto, testUser.getId()));
    }

    @Test
    void deleteById_IntegrationTest() {
        userService.deleteById(testUser.getId());

        Optional<User> deletedUser = userRepository.findById(testUser.getId());
        assertFalse(deletedUser.isPresent());
    }

    @Test
    void findById_IntegrationTest() {
        User result = userService.findById(testUser.getId());

        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals("Test User", result.getName());
    }

    @Test
    void findById_WhenUserNotExists_ShouldThrowException() {
        assertThrows(NotFoundException.class, () -> userService.findById(999L));
    }
}