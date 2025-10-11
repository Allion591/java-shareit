package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRequestRepositoryTest {

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private UserRepository userRepository;

    private User requester1;
    private User requester2;

    @BeforeEach
    void setUp() {
        itemRequestRepository.deleteAll();
        userRepository.deleteAll();

        requester1 = new User();
        requester1.setName("User 1");
        requester1.setEmail("user1@mail.com");
        requester1 = userRepository.save(requester1);

        requester2 = new User();
        requester2.setName("User 2");
        requester2.setEmail("user2@mail.com");
        requester2 = userRepository.save(requester2);
    }

    @Test
    void save_ShouldSaveItemRequest() {
        ItemRequest request = new ItemRequest();
        request.setDescription("Need a drill");
        request.setRequester(requester1);
        request.setCreated(LocalDateTime.now());

        ItemRequest saved = itemRequestRepository.save(request);

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals("Need a drill", saved.getDescription());
        assertEquals(requester1, saved.getRequester());
    }

    @Test
    void findByRequesterIdOrderByCreatedDesc_ShouldReturnUserRequests() {
        createTestRequest(requester1, "First request");
        createTestRequest(requester1, "Second request");

        List<ItemRequest> result = itemRequestRepository.findByRequesterIdOrderByCreatedDesc(requester1.getId());

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.get(0).getCreated().isAfter(result.get(1).getCreated()) ||
                result.get(0).getCreated().equals(result.get(1).getCreated()));
    }

    @Test
    void findByRequesterIdNotOrderByCreatedDesc_ShouldReturnOtherUsersRequests() {
        createTestRequest(requester1, "User1 request");
        createTestRequest(requester2, "User2 request");

        Pageable pageable = PageRequest.of(0, 10, Sort.by("created").descending());

        List<ItemRequest> result = itemRequestRepository.findByRequesterIdNotOrderByCreatedDesc(requester1.getId(), pageable);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("User2 request", result.get(0).getDescription());
    }

    @Test
    void getById_ShouldReturnRequest() {
        ItemRequest request = createTestRequest(requester1, "Test request");

        ItemRequest result = itemRequestRepository.getById(request.getId());

        assertNotNull(result);
        assertEquals(request.getId(), result.getId());
        assertEquals("Test request", result.getDescription());
    }

    @Test
    void deleteRequestById_ShouldDeleteRequest() {
        ItemRequest request = createTestRequest(requester1, "To be deleted");

        itemRequestRepository.deleteRequestById(request.getId());

        Optional<ItemRequest> deleted = itemRequestRepository.findById(request.getId());
        assertFalse(deleted.isPresent());
    }

    private ItemRequest createTestRequest(User user, String description) {
        ItemRequest request = new ItemRequest();
        request.setDescription(description);
        request.setRequester(user);
        request.setCreated(LocalDateTime.now());
        return itemRequestRepository.save(request);
    }
}