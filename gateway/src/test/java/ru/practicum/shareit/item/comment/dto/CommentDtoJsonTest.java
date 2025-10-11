package ru.practicum.shareit.item.comment.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentDtoJsonTest {

    @Autowired
    private JacksonTester<CommentDto> commentJson;

    @Autowired
    private JacksonTester<CommentResponseDto> commentResponseJson;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void shouldSerializeCommentDto() throws Exception {
        CommentDto commentDto = new CommentDto();
        commentDto.setText("Great item!");

        JsonContent<CommentDto> result = commentJson.write(commentDto);

        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("Great item!");
    }

    @Test
    void shouldDeserializeCommentDto() throws Exception {
        String content = "{\"text\":\"Great item!\"}";

        CommentDto result = commentJson.parseObject(content);

        assertThat(result.getText()).isEqualTo("Great item!");
    }

    @Test
    void shouldSerializeCommentResponseDto() throws Exception {
        CommentResponseDto responseDto = new CommentResponseDto();
        responseDto.setId(1L);
        responseDto.setText("Great item!");
        responseDto.setItemId(10L);
        responseDto.setAuthorName("John Doe");
        responseDto.setCreated(LocalDateTime.of(2023, 1, 1, 12, 0));

        JsonContent<CommentResponseDto> result = commentResponseJson.write(responseDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("Great item!");
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(10);
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo("John Doe");
        assertThat(result).hasJsonPathValue("$.created");
    }

    @Test
    void shouldDeserializeCommentResponseDto() throws Exception {
        String content = "{\"id\":1,\"text\":\"Great item!\",\"itemId\":10," +
                "\"authorName\":\"John Doe\",\"created\":\"2023-01-01T12:00:00\"}";

        CommentResponseDto result = commentResponseJson.parseObject(content);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getText()).isEqualTo("Great item!");
        assertThat(result.getItemId()).isEqualTo(10L);
        assertThat(result.getAuthorName()).isEqualTo("John Doe");
        assertThat(result.getCreated()).isEqualTo(LocalDateTime.of(2023, 1, 1, 12, 0));
    }

    @Test
    void shouldFailValidationWhenTextIsBlank() {
        CommentDto commentDto = new CommentDto();
        commentDto.setText(""); // Blank text

        Set<ConstraintViolation<CommentDto>> violations = validator.validate(commentDto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Текст комментария не может быть пустым");
    }
}