package ru.practicum.shareit.request.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RequestMapperTest {

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private RequestMapper requestMapper;

    @Test
    void toItemRequest_ShouldMapCorrectly() {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription("Need a drill");
        requestDto.setCreated(LocalDateTime.now());

        User requester = new User();
        requester.setId(1L);
        requester.setName("Requester");

        ItemRequest result = requestMapper.toItemRequest(requestDto, requester);

        assertNotNull(result);
        assertEquals("Need a drill", result.getDescription());
        assertEquals(requester, result.getRequester());
        assertEquals(requestDto.getCreated(), result.getCreated());
    }

    @Test
    void toResponseDto_ShouldMapCorrectly() {
        User requester = new User();
        requester.setId(1L);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("Need a drill");
        itemRequest.setRequester(requester);
        itemRequest.setCreated(LocalDateTime.now());

        ItemRequestResponseDto result = requestMapper.toResponseDto(itemRequest);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Need a drill", result.getDescription());
        assertEquals(1L, result.getRequester());
        assertEquals(itemRequest.getCreated(), result.getCreated());
    }

    @Test
    void toResponseDtoList_ShouldMapListCorrectly() {
        User requester = new User();
        requester.setId(1L);

        ItemRequest request1 = new ItemRequest();
        request1.setId(1L);
        request1.setDescription("First request");
        request1.setRequester(requester);

        ItemRequest request2 = new ItemRequest();
        request2.setId(2L);
        request2.setDescription("Second request");
        request2.setRequester(requester);

        List<ItemRequestResponseDto> result = requestMapper.toResponseDtoList(List.of(request1, request2));

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
        assertEquals("First request", result.get(0).getDescription());
        assertEquals("Second request", result.get(1).getDescription());
    }
}