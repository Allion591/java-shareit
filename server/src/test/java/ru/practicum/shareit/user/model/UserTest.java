package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void userCreation_ShouldSetFieldsCorrectly() {
        LocalDateTime now = LocalDateTime.now();

        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@mail.com");
        user.setRegistrationDate(now);

        assertEquals(1L, user.getId());
        assertEquals("Test User", user.getName());
        assertEquals("test@mail.com", user.getEmail());
        assertEquals(now, user.getRegistrationDate());
    }

    @Test
    void userEqualsAndHashCode_ShouldWorkCorrectly() {
        User user1 = new User();
        user1.setId(1L);
        user1.setName("User 1");

        User user2 = new User();
        user2.setId(1L);
        user2.setName("User 2");

        User user3 = new User();
        user3.setId(2L);
        user3.setName("User 1");

        assertEquals(user1, user1);
        assertEquals(user1.hashCode(), user1.hashCode());
        assertNotEquals(user1, user3);
    }

    @Test
    void userToString_ShouldContainRelevantInfo() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@mail.com");

        String toString = user.toString();

        assertTrue(toString.contains("Test User"));
        assertTrue(toString.contains("test@mail.com"));
    }

    @Test
    void userNoArgsConstructor_ShouldCreateEmptyObject() {
        User user = new User();

        assertNotNull(user);
        assertNull(user.getId());
        assertNull(user.getName());
        assertNull(user.getEmail());
        assertNull(user.getRegistrationDate());
    }
}