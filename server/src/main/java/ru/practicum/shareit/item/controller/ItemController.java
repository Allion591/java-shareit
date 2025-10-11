package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemResponseDto add(@RequestHeader("X-Sharer-User-Id") Long userId,
                               @RequestBody ItemDto itemDto) {
        log.info("Сервер принял запрос на сохранение новой вещи: {}", itemDto.getName());
        return itemService.create(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemResponseDto update(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @PathVariable Long itemId,
                                  @RequestBody ItemPatchDto itemPatchDto) {
        log.info("Сервер принял запрос на обновление новой вещи: {}, Ид: {}, владелец: {}", itemPatchDto.getName(),
                itemId, userId);
        return itemService.update(itemPatchDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ItemResponseDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                   @PathVariable Long itemId) {
        log.info("Сервер принял запрос на получение вещи: {}", itemId);
        return itemService.getById(itemId, userId);
    }

    @GetMapping
    public Collection<ItemResponseDto> get(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Сервер принял запрос на получение всех вещей пользователя: {}", userId);
        return itemService.getItems(userId);
    }

    @GetMapping("/search")
    public Collection<ItemResponseDto> search(@RequestParam String text,
                                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Сервер принял запрос на поиск вещи: {}", text);
        return itemService.search(text, userId);
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                           @PathVariable(name = "itemId") Long itemId) {
        log.info("Сервер принял запрос на удаление вещи: ид пользователя {}, ид вещи {}", userId, itemId);
        itemService.deleteItem(itemId, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @PathVariable Long itemId,
                                         @RequestBody CommentDto commentDto) {
        log.info("Сервер Пользователь {} оставляет комментарий к вещи {}", userId, itemId);
        return itemService.save(userId, itemId, commentDto);
    }
}