package ru.practicum.shareit.user.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {

    private Long id;

    private String name;

    private String email;

    private String login;

    private LocalDate birthday;

    public void setName(String name) {
        this.name = (name == null || name.isBlank()) ? null : name;
    }

    public String getName() {
        return (name == null || name.isBlank()) ? login : name;
    }
}