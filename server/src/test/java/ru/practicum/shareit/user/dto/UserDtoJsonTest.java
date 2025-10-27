package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserDtoJsonTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldSerializeUserDto() throws JsonProcessingException {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("Test User");
        userDto.setEmail("test@mail.com");
        userDto.setLogin("userlogin");
        userDto.setBirthday(LocalDate.of(1990, 1, 1));

        String json = objectMapper.writeValueAsString(userDto);

        assertThat(json).contains("\"id\":1");
        assertThat(json).contains("\"name\":\"Test User\"");
        assertThat(json).contains("\"email\":\"test@mail.com\"");
        assertThat(json).contains("\"login\":\"userlogin\"");
        assertThat(json).contains("\"birthday\":\"1990-01-01\"");
    }

    @Test
    void shouldDeserializeUserDto() throws JsonProcessingException {
        String json = "{\"id\":1,\"name\":\"Test User\",\"email\":\"test@mail.com\",\"login\":\"userlogin\"," +
                "\"birthday\":\"1990-01-01\"}";

        UserDto userDto = objectMapper.readValue(json, UserDto.class);

        assertThat(userDto.getId()).isEqualTo(1L);
        assertThat(userDto.getName()).isEqualTo("Test User");
        assertThat(userDto.getEmail()).isEqualTo("test@mail.com");
        assertThat(userDto.getLogin()).isEqualTo("userlogin");
        assertThat(userDto.getBirthday()).isEqualTo(LocalDate.of(1990, 1, 1));
    }

    @Test
    void getName_WhenNameIsBlank_ShouldReturnLogin() {
        UserDto userDto = new UserDto();
        userDto.setName("");
        userDto.setLogin("userlogin");

        String result = userDto.getName();

        assertThat(result).isEqualTo("userlogin");
    }

    @Test
    void getName_WhenNameIsNull_ShouldReturnLogin() {
        UserDto userDto = new UserDto();
        userDto.setName(null);
        userDto.setLogin("userlogin");

        String result = userDto.getName();

        assertThat(result).isEqualTo("userlogin");
    }

    @Test
    void getName_WhenNameIsProvided_ShouldReturnName() {
        UserDto userDto = new UserDto();
        userDto.setName("Test User");
        userDto.setLogin("userlogin");

        String result = userDto.getName();

        assertThat(result).isEqualTo("Test User");
    }
}