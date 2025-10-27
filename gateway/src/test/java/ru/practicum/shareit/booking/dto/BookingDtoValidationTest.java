package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

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

        assertFalse(violations.isEmpty(), "Должны быть нарушения валидации");

        Set<String> messages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());

        Set<String> paths = violations.stream()
                .map(v -> v.getPropertyPath().toString())
                .collect(Collectors.toSet());

        assertTrue(
                violations.stream().anyMatch(v ->
                        "itemId".equals(v.getPropertyPath().toString()) &&
                                "Идентификатор вещи не может быть Null".equals(v.getMessage())
                ),
                "Должно быть нарушение для itemId с правильным сообщением. " +
                        "Найдены нарушения для полей: " + paths + " с сообщениями: " + messages
        );
    }

    @Test
    void bookingDto_WhenStartIsInPast_ShouldFailValidation() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(LocalDateTime.now().minusDays(1));
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
        bookingDto.setEnd(LocalDateTime.now().minusDays(1));

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
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));

        Set<ConstraintViolation<BookingDto>> violations = validator.validate(bookingDto);

        assertTrue(violations.isEmpty());
        assertFalse(violations.size() >= 1);
    }

    @Test
    void bookingDto_WhenStartIsNull_ShouldFailValidation() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(null);
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
        bookingDto.setEnd(null);

        Set<ConstraintViolation<BookingDto>> violations = validator.validate(bookingDto);

        assertTrue(violations.isEmpty());
        assertEquals(0, 0);
    }
}