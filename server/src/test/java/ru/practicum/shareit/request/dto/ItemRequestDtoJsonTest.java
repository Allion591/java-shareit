package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestDtoJsonTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldSerializeItemRequestDto() throws JsonProcessingException {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setId(1L);
        requestDto.setDescription("Need a drill");
        requestDto.setCreated(LocalDateTime.of(2023, 12, 1, 10, 0));
        requestDto.setUserId(1L);

        String json = objectMapper.writeValueAsString(requestDto);

        assertThat(json).contains("\"id\":1");
        assertThat(json).contains("\"description\":\"Need a drill\"");
        assertThat(json).contains("\"created\":\"2023-12-01T10:00:00\"");
        assertThat(json).contains("\"userId\":1");
    }

    @Test
    void shouldDeserializeItemRequestDto() throws JsonProcessingException {
        String json = "{\"id\":1,\"description\":\"Need a drill\",\"created\":\"2023-12-01T10:00:00\",\"userId\":1}";

        ItemRequestDto requestDto = objectMapper.readValue(json, ItemRequestDto.class);

        assertThat(requestDto.getId()).isEqualTo(1L);
        assertThat(requestDto.getDescription()).isEqualTo("Need a drill");
        assertThat(requestDto.getCreated()).isEqualTo(LocalDateTime.of(2023, 12, 1, 10, 0));
        assertThat(requestDto.getUserId()).isEqualTo(1L);
    }
}