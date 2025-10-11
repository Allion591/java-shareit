package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserPatchDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto saveNewUser(@RequestBody UserDto userDto) {
        log.info("Сервер принял запрос на сохранение нового пользователя: {}", userDto.getName());
        return userService.create(userDto);
    }

    @GetMapping("/{userId}")
    public UserResponseDto getUserById(@PathVariable Long userId) {
        log.info("Сервер принял запрос на полученик пользователя по Ид: {}", userId);
        return userService.getById(userId);
    }

    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        log.info("Принял запрос на вывод списка всех пользователей");
        return userService.getAllUsers();
    }

    @PatchMapping("/{userId}")
    public UserResponseDto updateUser(@RequestBody UserPatchDto userPatchDto,
                                      @PathVariable Long userId) {
        log.info("Сервер принял запрос на обновление пользователя с Ид: {}", userId);
        return userService.update(userPatchDto, userId);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable Long userId) {
        log.info("Сервер принял запрос на удаления пользователя с Ид: {}", userId);
        userService.deleteById(userId);
    }
}