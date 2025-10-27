package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestMapper itemRequestMapper;
    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    @Transactional
    public ItemRequestResponseDto create(ItemRequestDto itemRequestDto, Long userId) {
        User user = userService.findById(userId);
        return itemRequestMapper.toResponseDto(itemRequestRepository.save(
                itemRequestMapper.toItemRequest(itemRequestDto, user)));
    }

    @Override
    @Transactional
    public ItemRequestResponseDto update(ItemRequest itemRequest) {
        return itemRequestMapper.toResponseDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestResponseDto> getAllByUser(Long userId) {
        userService.findById(userId);

        List<ItemRequest> requests = itemRequestRepository.findByRequesterIdOrderByCreatedDesc(userId);

        return requests.stream()
                .map(request -> {
                    ItemRequestResponseDto responseDto = itemRequestMapper.toResponseDto(request);
                    List<Item> items = itemRepository.findByRequestId(request.getId());
                    responseDto.setItems(itemMapper.itemResponseDtoShorts(items));
                    return responseDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestResponseDto> getAllOtherUsersRequests(Long userId, Integer from, Integer size) {
        userService.findById(userId);

        Pageable pageable = PageRequest.of(from / size, size, Sort.by("created").descending());

        List<ItemRequest> requests = itemRequestRepository.findByRequesterIdNotOrderByCreatedDesc(userId, pageable);

        return requests.stream()
                .map(request -> {
                    ItemRequestResponseDto responseDto = itemRequestMapper.toResponseDto(request);
                    List<Item> items = itemRepository.findByRequestId(request.getId());
                    responseDto.setItems(itemMapper.itemResponseDtoShorts(items));
                    return responseDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteRequestById(Long requestId) {
        itemRequestRepository.deleteRequestById(requestId);
    }

    @Override
    public ItemRequestResponseDto getById(Long requestId, Long userId) {
        userService.getById(userId);

        ItemRequest request = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос с ID " + requestId + " не найден"));

        List<Item> items = itemRepository.findByRequestId(requestId);

        ItemRequestResponseDto responseDto = itemRequestMapper.toResponseDto(request);
        responseDto.setItems(itemMapper.itemResponseDtoShorts(items));

        return responseDto;
    }
}