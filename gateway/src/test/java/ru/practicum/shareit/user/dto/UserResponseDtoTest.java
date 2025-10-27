package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserResponseDtoTest {

    @Autowired
    private JacksonTester<UserResponseDto> json;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void userResponseDtoSerializationTest() throws Exception {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(1L);
        userResponseDto.setName("John Doe");
        userResponseDto.setEmail("john@example.com");
        userResponseDto.setLogin("johndoe");

        var result = json.write(userResponseDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("John Doe");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("john@example.com");
        assertThat(result).extractingJsonPathStringValue("$.login").isEqualTo("johndoe");
    }

    @Test
    void userResponseDtoDeserializationTest() throws Exception {
        String jsonContent = "{\"id\": 1, "
                + "\"name\": \"John Doe\", "
                + "\"email\": \"john@example.com\", "
                + "\"login\": \"johndoe\"}";

        UserResponseDto userResponseDto = objectMapper.readValue(jsonContent, UserResponseDto.class);

        assertThat(userResponseDto.getId()).isEqualTo(1L);
        assertThat(userResponseDto.getName()).isEqualTo("John Doe");
        assertThat(userResponseDto.getEmail()).isEqualTo("john@example.com");
        assertThat(userResponseDto.getLogin()).isEqualTo("johndoe");
    }
}