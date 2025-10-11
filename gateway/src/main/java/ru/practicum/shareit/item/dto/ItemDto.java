package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class ItemDto {
    private Long id;
    private Long ownerId;

    @NotBlank(message = "Не указано название вещи")
    private String name;

    @NotBlank(message = "Не указано описание вещи")
    private String description;

    @PositiveOrZero(message = "ID запроса должен быть положительным числом или 0")
    private Long requestId;

    @NotNull(message = "Не указана занятость вещи")
    private Boolean available;
}