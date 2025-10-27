package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class RequestServiceIntegrationTest {

    @Autowired
    private RequestService requestService;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private UserRepository userRepository;

    private User requester;
    private User otherUser;

    @BeforeEach
    void setUp() {
        itemRequestRepository.deleteAll();
        userRepository.deleteAll();

        requester = new User();
        requester.setName("Requester");
        requester.setEmail("requester@mail.com");
        requester = userRepository.save(requester);

        otherUser = new User();
        otherUser.setName("Other User");
        otherUser.setEmail("other@mail.com");
        otherUser = userRepository.save(otherUser);
    }

    @Test
    void create_IntegrationTest() {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription("Need a drill");
        requestDto.setCreated(LocalDateTime.now());

        ItemRequestResponseDto result = requestService.create(requestDto, requester.getId());

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Need a drill", result.getDescription());
        assertEquals(requester.getId(), result.getRequester());

        var savedRequest = itemRequestRepository.findById(result.getId());
        assertTrue(savedRequest.isPresent());
        assertEquals("Need a drill", savedRequest.get().getDescription());
    }

    @Test
    void getAllByUser_IntegrationTest() {
        createTestRequest(requester, "First request");
        createTestRequest(requester, "Second request");

        List<ItemRequestResponseDto> result = requestService.getAllByUser(requester.getId());

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(r -> r.getDescription().equals("First request")));
        assertTrue(result.stream().anyMatch(r -> r.getDescription().equals("Second request")));
    }

    @Test
    void getAllOtherUsersRequests_IntegrationTest() {
        createTestRequest(requester, "Requester's request");
        createTestRequest(otherUser, "Other user's request");

        List<ItemRequestResponseDto> result = requestService.getAllOtherUsersRequests(requester.getId(), 0, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Other user's request", result.get(0).getDescription());
    }

    @Test
    void getById_IntegrationTest() {
        ItemRequest request = createTestRequest(requester, "Test request");

        ItemRequestResponseDto result = requestService.getById(request.getId(), requester.getId());

        assertNotNull(result);
        assertEquals(request.getId(), result.getId());
        assertEquals("Test request", result.getDescription());
        assertEquals(requester.getId(), result.getRequester());
    }

    private ItemRequest createTestRequest(User user, String description) {
        ItemRequest request = new ItemRequest();
        request.setDescription(description);
        request.setRequester(user);
        request.setCreated(LocalDateTime.now());
        return itemRequestRepository.save(request);
    }
}