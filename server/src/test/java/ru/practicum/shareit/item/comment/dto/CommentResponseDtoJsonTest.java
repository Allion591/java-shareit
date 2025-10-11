package ru.practicum.shareit.item.comment.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentResponseDtoJsonTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldSerializeCommentResponseDto() throws JsonProcessingException {
        CommentResponseDto responseDto = new CommentResponseDto();
        responseDto.setId(1L);
        responseDto.setText("Great item!");
        responseDto.setAuthorName("Booker");
        responseDto.setCreated(LocalDateTime.of(2023, 12, 1, 10, 0));
        responseDto.setItemId(1L);

        String json = objectMapper.writeValueAsString(responseDto);

        assertThat(json).contains("\"id\":1");
        assertThat(json).contains("\"text\":\"Great item!\"");
        assertThat(json).contains("\"authorName\":\"Booker\"");
        assertThat(json).contains("\"created\":\"2023-12-01T10:00:00\"");
        assertThat(json).contains("\"itemId\":1");
    }

    @Test
    void shouldDeserializeCommentResponseDto() throws JsonProcessingException {
        String json = "{\"id\":1,\"text\":\"Great item!\",\"authorName\":\"Booker\",\"created\":\"2023-12-01T10:00:00\",\"itemId\":1}";

        CommentResponseDto responseDto = objectMapper.readValue(json, CommentResponseDto.class);

        assertThat(responseDto.getId()).isEqualTo(1L);
        assertThat(responseDto.getText()).isEqualTo("Great item!");
        assertThat(responseDto.getAuthorName()).isEqualTo("Booker");
        assertThat(responseDto.getCreated()).isEqualTo(LocalDateTime.of(2023, 12, 1, 10, 0));
        assertThat(responseDto.getItemId()).isEqualTo(1L);
    }
}