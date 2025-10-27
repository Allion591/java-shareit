package ru.practicum.shareit.item.comment.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentDtoJsonTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldSerializeCommentDto() throws JsonProcessingException {
        CommentDto commentDto = new CommentDto();
        commentDto.setText("Great item!");

        String json = objectMapper.writeValueAsString(commentDto);

        assertThat(json).contains("\"text\":\"Great item!\"");
    }

    @Test
    void shouldDeserializeCommentDto() throws JsonProcessingException {
        String json = "{\"text\":\"Great item!\"}";

        CommentDto commentDto = objectMapper.readValue(json, CommentDto.class);

        assertThat(commentDto.getText()).isEqualTo("Great item!");
    }
}