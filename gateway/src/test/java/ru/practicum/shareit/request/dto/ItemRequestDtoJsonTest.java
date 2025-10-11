package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestDtoJsonTest {

    @Autowired
    private JacksonTester<ItemRequestDto> json;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void shouldSerializeItemRequestDto() throws Exception {
        // Given
        ItemResponseDto itemResponse = new ItemResponseDto();
        itemResponse.setId(1L);
        itemResponse.setName("Drill");
        itemResponse.setDescription("Powerful drill");
        itemResponse.setAvailable(true);

        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setId(1L);
        requestDto.setDescription("Need a drill for home repairs");
        requestDto.setCreated(LocalDateTime.of(2023, 1, 1, 12, 0));
        requestDto.setItems(List.of(itemResponse));

        JsonContent<ItemRequestDto> result = json.write(requestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("Need a drill for home repairs");
        assertThat(result).hasJsonPathValue("$.created");
        assertThat(result).hasJsonPathArrayValue("$.items");
        assertThat(result).extractingJsonPathNumberValue("$.items[0].id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.items[0].name").isEqualTo("Drill");
    }

    @Test
    void shouldDeserializeItemRequestDto() throws Exception {
        String content = "{\"id\":1,\"description\":\"Need a drill for home repairs\"," +
                "\"created\":\"2023-01-01T12:00:00\"," +
                "\"items\":[{\"id\":1,\"name\":\"Drill\",\"description\":\"Powerful drill\",\"available\":true}]}";

        ItemRequestDto result = json.parseObject(content);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDescription()).isEqualTo("Need a drill for home repairs");
        assertThat(result.getCreated()).isEqualTo(LocalDateTime.of(2023, 1, 1, 12, 0));
        assertThat(result.getItems()).hasSize(1);
        assertThat(result.getItems().get(0).getName()).isEqualTo("Drill");
    }

    @Test
    void shouldDeserializeItemRequestDtoWithoutItems() throws Exception {
        String content = "{\"id\":1,\"description\":\"Need a drill for home repairs\"," +
                "\"created\":\"2023-01-01T12:00:00\"}";

        ItemRequestDto result = json.parseObject(content);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDescription()).isEqualTo("Need a drill for home repairs");
        assertThat(result.getCreated()).isEqualTo(LocalDateTime.of(2023, 1, 1, 12, 0));
        assertThat(result.getItems()).isNull();
    }

    @Test
    void shouldFailValidationWhenDescriptionIsNull() {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription(null);

        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(requestDto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("не должно быть пустым");
    }

    @Test
    void shouldFailValidationWhenDescriptionIsBlank() {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription("");

        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(requestDto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("не должно быть пустым");
    }

    @Test
    void shouldFailValidationWhenDescriptionIsTooLong() {
        ItemRequestDto requestDto = new ItemRequestDto();
        String longDescription = "a".repeat(1001);
        requestDto.setDescription(longDescription);

        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(requestDto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Описание не может превышать 1000 символов");
    }

    @Test
    void shouldPassValidationWhenDescriptionIsValid() {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription("Valid description");

        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(requestDto);

        assertThat(violations).isEmpty();
    }

    @Test
    void shouldPassValidationWhenDescriptionIsExactlyMaxLength() {
        ItemRequestDto requestDto = new ItemRequestDto();
        String maxLengthDescription = "a".repeat(1000);
        requestDto.setDescription(maxLengthDescription);

        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(requestDto);

        assertThat(violations).isEmpty();
    }
}