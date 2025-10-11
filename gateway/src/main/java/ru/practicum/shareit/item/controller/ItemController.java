package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> add(@RequestHeader("X-Sharer-User-Id") Long userId,
                                      @RequestBody @Valid ItemDto itemDto) {
        log.info("Принял запрос на сохранение новой вещи: {}", itemDto.getName());
        return itemClient.saveNewItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @PathVariable Long itemId,
                                         @RequestBody ItemPatchDto itemPatchDto) {
        log.info("Шлюз принял запрос на обновление новой вещи: {}, Ид: {}, владелец: {}", itemPatchDto.getName(),
                itemId, userId);
        return itemClient.update(itemPatchDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PathVariable Long itemId) {
        log.info("Шлюз принял запрос на получение вещи: {}", itemId);
        return itemClient.getById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<List<Object>> get(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Шлюз принял запрос на получение всех вещей пользователя: {}", userId);
        return itemClient.getItems(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Object>> search(@RequestParam String text,
                                               @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Шлюз принял запрос на поиск вещи: {}", text);
        return itemClient.search(text, userId);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                           @PathVariable(name = "itemId") Long itemId) {
        log.info("Шлюз принял запрос на удаление вещи: ид пользователя {}, ид вещи {}", userId, itemId);
        itemClient.deleteItem(itemId, userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable Long itemId,
                                             @RequestBody @Valid CommentDto commentDto) {
        log.info("Шлюз пользователь {} оставляет комментарий к вещи {}", userId, itemId);
        return itemClient.saveComment(userId, itemId, commentDto);
    }
}