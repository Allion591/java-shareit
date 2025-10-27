package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestParams;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestBody @Valid BookingDto bookingDto,
                                                @RequestHeader("X-Sharer-User-Id") @Min(1) Long userId) {
        log.info("Шлюз принял запрос на сохранение бронирования: {}, {}", bookingDto.getItemId(), userId);
        return bookingClient.create(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBookingStatus(@PathVariable @Min(1) Long bookingId,
                                                      @RequestParam @NotNull Boolean approved,
                                                      @RequestHeader("X-Sharer-User-Id") @Min(1) Long userId) {
        log.info("Шлюз принял запрос на обновление бронирования: {}, {}", bookingId, userId);
        return bookingClient.updateStatus(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@PathVariable @Min(1) Long bookingId,
                                             @RequestHeader("X-Sharer-User-Id") @Min(1) Long userId) {
        log.info("Шлюз принял запрос на вывод бронирования: {}, {}", bookingId, userId);
        return bookingClient.getById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<List<Object>> getUserBookings1(@RequestHeader("X-Sharer-User-Id")
                                                             @Min(1) Long userId,
            @ModelAttribute @Valid BookingRequestParams params) {
        log.info("Шлюз принял запрос на вывод всех броней пользователя: {}", userId);
        return bookingClient.getUserBookings(userId, params.getState(), params.getFrom(), params.getSize());
    }

    @GetMapping("/owner")
    public ResponseEntity<List<Object>> getOwnerBookings(@RequestHeader("X-Sharer-User-Id")
                                                             @Min(1) Long userId,
                                                         @ModelAttribute @Valid BookingRequestParams params) {
        log.info("Шлюз принял запрос на вывод всех броней владельца: {}", userId);
        return bookingClient.getOwnerBookings(userId, params.getState(), params.getFrom(), params.getSize());
    }
}