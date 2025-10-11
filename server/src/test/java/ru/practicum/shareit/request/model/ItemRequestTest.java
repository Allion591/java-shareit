package ru.practicum.shareit.request.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestTest {

    @Test
    void itemRequestCreation_ShouldSetFieldsCorrectly() {
        User requester = new User();
        requester.setId(1L);
        requester.setName("Requester");

        LocalDateTime created = LocalDateTime.now();

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("Need a drill");
        itemRequest.setRequester(requester);
        itemRequest.setCreated(created);

        assertEquals(1L, itemRequest.getId());
        assertEquals("Need a drill", itemRequest.getDescription());
        assertEquals(requester, itemRequest.getRequester());
        assertEquals(created, itemRequest.getCreated());
    }

    @Test
    void itemRequestEqualsAndHashCode_ShouldWorkCorrectly() {
        ItemRequest request1 = new ItemRequest();
        request1.setId(1L);

        ItemRequest request2 = new ItemRequest();
        request2.setId(1L);

        ItemRequest request3 = new ItemRequest();
        request3.setId(2L);

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1, request3);
    }

    @Test
    void itemRequest_ShouldCreateObject() {
        User requester = new User();
        requester.setId(1L);

        LocalDateTime created = LocalDateTime.now();

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("Need a drill");
        itemRequest.setRequester(requester);
        itemRequest.setCreated(created);

        assertNotNull(itemRequest);
        assertEquals(1L, itemRequest.getId());
        assertEquals("Need a drill", itemRequest.getDescription());
        assertEquals(requester, itemRequest.getRequester());
        assertEquals(created, itemRequest.getCreated());
    }

    @Test
    void itemRequestToString_ShouldContainRelevantInfo() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("Need a drill");

        String toString = itemRequest.toString();

        assertTrue(toString.contains("Need a drill"));
    }
}