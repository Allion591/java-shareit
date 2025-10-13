package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemResponseDtoShort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.RequestMapper;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplTest {

    @Mock
    private RequestMapper requestMapper;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private UserService userService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private RequestServiceImpl requestService;

    @Test
    void create_ShouldCreateRequest() {
        Long userId = 1L;
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription("Need a drill");

        User user = new User();
        user.setId(userId);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription("Need a drill");

        ItemRequest savedRequest = new ItemRequest();
        savedRequest.setId(1L);
        savedRequest.setDescription("Need a drill");

        ItemRequestResponseDto responseDto = new ItemRequestResponseDto();
        responseDto.setId(1L);
        responseDto.setDescription("Need a drill");

        when(userService.findById(userId)).thenReturn(user);
        when(requestMapper.toItemRequest(any(ItemRequestDto.class), any(User.class))).thenReturn(itemRequest);
        when(itemRequestRepository.save(any(ItemRequest.class))).thenReturn(savedRequest);
        when(requestMapper.toResponseDto(any(ItemRequest.class))).thenReturn(responseDto);

        ItemRequestResponseDto result = requestService.create(requestDto, userId);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Need a drill", result.getDescription());
        verify(userService).findById(userId);
        verify(itemRequestRepository).save(any(ItemRequest.class));
    }

    @Test
    void getAllByUser_ShouldReturnUserRequests() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        ItemRequest request1 = new ItemRequest();
        request1.setId(1L);
        request1.setDescription("First request");

        ItemRequest request2 = new ItemRequest();
        request2.setId(2L);
        request2.setDescription("Second request");

        ItemRequestResponseDto responseDto1 = new ItemRequestResponseDto();
        responseDto1.setId(1L);
        responseDto1.setDescription("First request");

        ItemRequestResponseDto responseDto2 = new ItemRequestResponseDto();
        responseDto2.setId(2L);
        responseDto2.setDescription("Second request");

        when(userService.findById(userId)).thenReturn(user);
        when(itemRequestRepository.findByRequesterIdOrderByCreatedDesc(userId)).thenReturn(List.of(request1, request2));
        when(requestMapper.toResponseDto(any(ItemRequest.class))).thenReturn(responseDto1, responseDto2);
        when(itemRepository.findByRequestId(anyLong())).thenReturn(List.of());
        when(itemMapper.itemResponseDtoShorts(anyList())).thenReturn(List.of());

        List<ItemRequestResponseDto> result = requestService.getAllByUser(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(itemRequestRepository).findByRequesterIdOrderByCreatedDesc(userId);
    }

    @Test
    void getAllOtherUsersRequests_ShouldReturnOtherUsersRequests() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        ItemRequest otherRequest = new ItemRequest();
        otherRequest.setId(2L);
        otherRequest.setDescription("Other user request");

        ItemRequestResponseDto responseDto = new ItemRequestResponseDto();
        responseDto.setId(2L);
        responseDto.setDescription("Other user request");

        Pageable pageable = PageRequest.of(0, 10, Sort.by("created").descending());

        when(userService.findById(userId)).thenReturn(user);
        when(itemRequestRepository.findByRequesterIdNotOrderByCreatedDesc(eq(userId), any(Pageable.class)))
                .thenReturn(List.of(otherRequest));
        when(requestMapper.toResponseDto(any(ItemRequest.class))).thenReturn(responseDto);
        when(itemRepository.findByRequestId(anyLong())).thenReturn(List.of());
        when(itemMapper.itemResponseDtoShorts(anyList())).thenReturn(List.of());

        List<ItemRequestResponseDto> result = requestService.getAllOtherUsersRequests(userId, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Other user request", result.get(0).getDescription());
        verify(itemRequestRepository).findByRequesterIdNotOrderByCreatedDesc(eq(userId), any(Pageable.class));
    }

    @Test
    void getById_WhenRequestExists_ShouldReturnRequest() {
        Long requestId = 1L;
        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        ItemRequest request = new ItemRequest();
        request.setId(requestId);
        request.setDescription("Test request");

        ItemRequestResponseDto responseDto = new ItemRequestResponseDto();
        responseDto.setId(requestId);
        responseDto.setDescription("Test request");

        when(userService.getById(userId)).thenReturn(null);
        when(itemRequestRepository.findById(requestId)).thenReturn(Optional.of(request));
        when(requestMapper.toResponseDto(any(ItemRequest.class))).thenReturn(responseDto);
        when(itemRepository.findByRequestId(requestId)).thenReturn(List.of());
        when(itemMapper.itemResponseDtoShorts(anyList())).thenReturn(List.of());

        ItemRequestResponseDto result = requestService.getById(requestId, userId);

        assertNotNull(result);
        assertEquals(requestId, result.getId());
        assertEquals("Test request", result.getDescription());
    }

    @Test
    void getById_WhenRequestNotExists_ShouldThrowNotFoundException() {
        Long requestId = 999L;
        Long userId = 1L;

        when(userService.getById(userId)).thenReturn(null);
        when(itemRequestRepository.findById(requestId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> requestService.getById(requestId, userId));
        verify(itemRequestRepository).findById(requestId);
    }

    @Test
    void update_ShouldUpdateRequest() {
        ItemRequest request = new ItemRequest();
        request.setId(1L);
        request.setDescription("Updated request");

        ItemRequestResponseDto responseDto = new ItemRequestResponseDto();
        responseDto.setId(1L);
        responseDto.setDescription("Updated request");

        when(itemRequestRepository.save(any(ItemRequest.class))).thenReturn(request);
        when(requestMapper.toResponseDto(any(ItemRequest.class))).thenReturn(responseDto);

        ItemRequestResponseDto result = requestService.update(request);

        assertNotNull(result);
        assertEquals("Updated request", result.getDescription());
        verify(itemRequestRepository).save(request);
    }

    @Test
    void deleteRequestById_ShouldDeleteRequest() {
        Long requestId = 1L;

        doNothing().when(itemRequestRepository).deleteRequestById(requestId);

        requestService.deleteRequestById(requestId);

        verify(itemRequestRepository).deleteRequestById(requestId);
    }

    @Test
    void getAllByUser_WithItems_ShouldReturnRequestsWithItems() {
        Long userId = 1L;
        Long requestId = 1L;

        User user = new User();
        user.setId(userId);

        ItemRequest request = new ItemRequest();
        request.setId(requestId);
        request.setDescription("Request with items");

        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item");

        ItemResponseDtoShort itemResponseDto = new ItemResponseDtoShort();
        itemResponseDto.setId(1L);
        itemResponseDto.setName("Test Item");

        ItemRequestResponseDto responseDto = new ItemRequestResponseDto();
        responseDto.setId(requestId);
        responseDto.setDescription("Request with items");

        when(userService.findById(userId)).thenReturn(user);
        when(itemRequestRepository.findByRequesterIdOrderByCreatedDesc(userId)).thenReturn(List.of(request));
        when(requestMapper.toResponseDto(any(ItemRequest.class))).thenReturn(responseDto);
        when(itemRepository.findByRequestId(requestId)).thenReturn(List.of(item));
        when(itemMapper.itemResponseDtoShorts(anyList())).thenReturn(List.of(itemResponseDto));

        List<ItemRequestResponseDto> result = requestService.getAllByUser(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertNotNull(result.get(0).getItems());
        assertEquals(1, result.get(0).getItems().size());
        verify(itemRepository).findByRequestId(requestId);
    }
}