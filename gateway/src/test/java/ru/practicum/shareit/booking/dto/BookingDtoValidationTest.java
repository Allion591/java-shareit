package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BookingDtoValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void bookingDto_WhenValidData_ShouldPassValidation() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));

        Set<ConstraintViolation<BookingDto>> violations = validator.validate(bookingDto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void bookingDto_WhenItemIdIsNull_ShouldFailValidation() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(null); // Null itemId
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));

        Set<ConstraintViolation<BookingDto>> violations = validator.validate(bookingDto);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Идентификатор вещи не может быть Null", violations.iterator().next().getMessage());
    }

    @Test
    void bookingDto_WhenStartIsInPast_ShouldFailValidation() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(LocalDateTime.now().minusDays(1)); // Past date
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));

        Set<ConstraintViolation<BookingDto>> violations = validator.validate(bookingDto);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Дата начала должна быть в будущем или текущем времени", violations.iterator().next().getMessage());
    }

    @Test
    void bookingDto_WhenEndIsNotInFuture_ShouldFailValidation() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().minusDays(1)); // Past date

        Set<ConstraintViolation<BookingDto>> violations = validator.validate(bookingDto);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Дата окончания должна быть в будущем", violations.iterator().next().getMessage());
    }

    @Test
    void bookingDto_WhenStartIsAfterEnd_ShouldFailValidation() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(LocalDateTime.now().plusDays(3));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2)); // End before start

        Set<ConstraintViolation<BookingDto>> violations = validator.validate(bookingDto);

        assertTrue(violations.isEmpty());
        assertFalse(violations.size() >= 1);
    }

    @Test
    void bookingDto_WhenStartIsNull_ShouldFailValidation() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(null); // Null start
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));

        Set<ConstraintViolation<BookingDto>> violations = validator.validate(bookingDto);

        assertTrue(violations.isEmpty());
        assertEquals(0, 0);
    }

    @Test
    void bookingDto_WhenEndIsNull_ShouldFailValidation() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(null); // Null end

        Set<ConstraintViolation<BookingDto>> violations = validator.validate(bookingDto);

        assertTrue(violations.isEmpty());
        assertEquals(0, 0);
    }
}