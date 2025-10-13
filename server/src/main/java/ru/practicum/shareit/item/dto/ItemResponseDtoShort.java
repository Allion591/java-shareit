package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class ItemResponseDtoShort {
    private Long id;
    private Long owner;
    private String name;
}
