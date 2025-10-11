package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserResponseDtoJsonTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldSerializeUserResponseDto() throws JsonProcessingException {
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(1L);
        responseDto.setName("Test User");
        responseDto.setEmail("test@mail.com");
        responseDto.setLogin("userlogin");

        String json = objectMapper.writeValueAsString(responseDto);

        assertThat(json).contains("\"id\":1");
        assertThat(json).contains("\"name\":\"Test User\"");
        assertThat(json).contains("\"email\":\"test@mail.com\"");
        assertThat(json).contains("\"login\":\"userlogin\"");
    }

    @Test
    void shouldDeserializeUserResponseDto() throws JsonProcessingException {
        String json = "{\"id\":1,\"name\":\"Test User\",\"email\":\"test@mail.com\",\"login\":\"userlogin\"}";

        UserResponseDto responseDto = objectMapper.readValue(json, UserResponseDto.class);

        assertThat(responseDto.getId()).isEqualTo(1L);
        assertThat(responseDto.getName()).isEqualTo("Test User");
        assertThat(responseDto.getEmail()).isEqualTo("test@mail.com");
        assertThat(responseDto.getLogin()).isEqualTo("userlogin");
    }
}