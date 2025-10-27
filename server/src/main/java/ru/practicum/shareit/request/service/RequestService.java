package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import java.util.List;

public interface RequestService {

    ItemRequestResponseDto create(ItemRequestDto itemRequestDto, Long userId);

    ItemRequestResponseDto update(ItemRequest itemRequest);

    void deleteRequestById(Long requestId);

    ItemRequestResponseDto getById(Long requestId, Long userId);

    List<ItemRequestResponseDto> getAllByUser(Long userId);

    List<ItemRequestResponseDto> getAllOtherUsersRequests(Long userId, Integer from, Integer size);
}
