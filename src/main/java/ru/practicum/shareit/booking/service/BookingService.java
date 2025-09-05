package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.Collection;

public interface BookingService {

    BookingResponseDto create(Booking booking, Long userId);

    BookingResponseDto update(Long bookingId, Long userId, Boolean approved);

    BookingResponseDto getById(Long userId, Long bookingId);

    Collection<BookingResponseDto> getAll(Long userId);

    void deleteById(Long bookingId);
}
