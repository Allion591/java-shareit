package ru.practicum.shareit.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserPatchDto;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> saveNewUser(@RequestBody @Valid UserDto userDto) {
        log.info("Шлюз принял запрос на сохранение нового пользователя: {}", userDto.getName());
        return userClient.saveNewUser(userDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable Long userId) {
        log.info("Шлюз принял запрос на получение пользователя по Ид: {}", userId);
        return userClient.getById(userId);
    }

    @GetMapping
    public ResponseEntity<List<Object>> getAllUsers() {
        log.info("Шлюз принял запрос на вывод списка всех пользователей");
        return userClient.getAllUsers();
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable Long userId,
                                             @RequestBody UserPatchDto userPatchDto) {
        log.info("Шлюз принял запрос на обновление пользователя с Ид: {}", userId);
        return userClient.update(userId, userPatchDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable Long userId) {
        log.info("Шлюз принял запрос на удаления пользователя с Ид: {}", userId);
        userClient.deleteById(userId);
    }
}