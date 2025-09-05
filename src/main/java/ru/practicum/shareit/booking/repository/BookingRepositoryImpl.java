package ru.practicum.shareit.booking.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;

import java.util.ArrayList;
import java.util.Collection;

@Repository
public class BookingRepositoryImpl implements BookingRepository {
    Booking booking = new Booking();

    @Override
    public Booking save(Booking booking) {
        return booking;
    }

    @Override
    public Booking update(Booking booking) {
        return booking;
    }

    @Override
    public Booking getById(Long bookingId) {
        return booking;
    }

    @Override
    public Collection<Booking> getAll(Long userId) {
        return new ArrayList<>();
    }

    @Override
    public void deleteById(Long bookingId) {

    }
}
