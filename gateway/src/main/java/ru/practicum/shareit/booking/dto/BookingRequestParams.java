package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class BookingRequestParams {

    @Pattern(regexp = "ALL|WAITING|REJECTED|APPROVED|CANCELED",
            message = "Неизвестный статус: ${validatedValue}")
    private String state = "ALL";

    @Min(value = 0, message = "Начало должно быть не ниже ноля")
    private Integer from = 0;

    @Min(value = 1, message = "Минимальное значение 1")
    @Max(value = 50, message = "максимальное значение 50")
    private Integer size = 10;
}