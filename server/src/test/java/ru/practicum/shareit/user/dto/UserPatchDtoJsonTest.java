package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserPatchDtoJsonTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldSerializeUserPatchDto() throws JsonProcessingException {
        UserPatchDto patchDto = new UserPatchDto();
        patchDto.setName(Optional.of("Updated User"));
        patchDto.setEmail(Optional.of("updated@mail.com"));
        patchDto.setLogin(Optional.of("updatedlogin"));
        patchDto.setBirthday(Optional.of(LocalDate.of(1990, 1, 1)));

        String json = objectMapper.writeValueAsString(patchDto);

        assertThat(json).contains("\"name\":\"Updated User\"");
        assertThat(json).contains("\"email\":\"updated@mail.com\"");
        assertThat(json).contains("\"login\":\"updatedlogin\"");
        assertThat(json).contains("\"birthday\":\"1990-01-01\"");
    }

    @Test
    void shouldDeserializeUserPatchDto() throws JsonProcessingException {
        String json = "{\"name\":\"Updated User\",\"email\":\"updated@mail.com\",\"login\":" +
                "\"updatedlogin\",\"birthday\":\"1990-01-01\"}";

        UserPatchDto patchDto = objectMapper.readValue(json, UserPatchDto.class);

        assertThat(patchDto.getName()).isPresent().contains("Updated User");
        assertThat(patchDto.getEmail()).isPresent().contains("updated@mail.com");
        assertThat(patchDto.getLogin()).isPresent().contains("updatedlogin");
        assertThat(patchDto.getBirthday()).isPresent().contains(LocalDate.of(1990, 1, 1));
    }

    @Test
    void shouldHandleEmptyOptionals() throws JsonProcessingException {
        String json = "{}";

        UserPatchDto patchDto = objectMapper.readValue(json, UserPatchDto.class);

        assertThat(patchDto.getName()).isNotPresent();
        assertThat(patchDto.getEmail()).isNotPresent();
        assertThat(patchDto.getLogin()).isNotPresent();
        assertThat(patchDto.getBirthday()).isNotPresent();
    }
}