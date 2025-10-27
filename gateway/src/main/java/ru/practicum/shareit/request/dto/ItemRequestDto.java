package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemRequestDto {
    private Long id;

    @NotBlank
    @Size(max = 1000, message = "Описание не может превышать 1000 символов")
    private String description;

    private LocalDateTime created;
    private List<ItemResponseDto> items;
}