package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;
import java.time.LocalDate;

@Data
public class UserPatchDto {
    private String name;
    @Email(message = "Неверный формат электронной почты")
    private String email;
    private String login;
    private LocalDate birthday;
}