package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

import java.time.LocalDate;
import java.util.Optional;

@Data
public class UserPatchDto {
    private Optional<Long> id;
    private Optional<String> name;
    @Email(message = "Неверный формат электронной почты")
    private Optional<String> email;
    private Optional<String> login;
    private Optional<LocalDate> birthday;
}