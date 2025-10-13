package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.item.dto.ItemResponseDtoShort;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemRequestResponseDto {
    private Long id;
    private String description;
    private Long requester;
    private LocalDateTime created;
    private List<ItemResponseDtoShort> items;
}