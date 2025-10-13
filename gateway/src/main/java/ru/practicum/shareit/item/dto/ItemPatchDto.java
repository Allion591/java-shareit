package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ItemPatchDto {
    private Long id;

    @Size(min = 1, max = 255, message = "Название вещи должно быть от 1 до 255 символов")
    private String name;

    @Size(max = 255, message = "Описание вещи не должно превышать 255 символов")
    private String description;

    @Positive
    private Long requestId;

    private Boolean available;
}