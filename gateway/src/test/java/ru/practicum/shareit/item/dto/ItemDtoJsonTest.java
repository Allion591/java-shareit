package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDtoJsonTest {

    @Autowired
    private JacksonTester<ItemDto> json;

    @Autowired
    private ObjectMapper objectMapper;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void shouldSerializeItemDto() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setOwnerId(2L);
        itemDto.setName("Test Item");
        itemDto.setDescription("Test Description");
        itemDto.setAvailable(true);
        itemDto.setRequestId(5L);

        JsonContent<ItemDto> result = json.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.ownerId").isEqualTo(2);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Test Item");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Test Description");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(5);
    }

    @Test
    void shouldDeserializeItemDto() throws Exception {
        String content = "{\"id\":1,\"ownerId\":2,\"name\":\"Test Item\"," +
                "\"description\":\"Test Description\",\"available\":true,\"requestId\":5}";

        ItemDto result = json.parseObject(content);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getOwnerId()).isEqualTo(2L);
        assertThat(result.getName()).isEqualTo("Test Item");
        assertThat(result.getDescription()).isEqualTo("Test Description");
        assertThat(result.getAvailable()).isEqualTo(true);
        assertThat(result.getRequestId()).isEqualTo(5L);
    }

    @Test
    void shouldFailValidationWhenNameIsBlank() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName(""); // Blank name
        itemDto.setDescription("Valid description");
        itemDto.setAvailable(true);

        Set<ConstraintViolation<ItemDto>> violations = validator.validate(itemDto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Не указано название вещи");
    }

    @Test
    void shouldFailValidationWhenDescriptionIsBlank() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Valid name");
        itemDto.setDescription(""); // Blank description
        itemDto.setAvailable(true);

        Set<ConstraintViolation<ItemDto>> violations = validator.validate(itemDto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Не указано описание вещи");
    }

    @Test
    void shouldFailValidationWhenAvailableIsNull() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Valid name");
        itemDto.setDescription("Valid description");
        itemDto.setAvailable(null); // Null available

        Set<ConstraintViolation<ItemDto>> violations = validator.validate(itemDto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Не указана занятость вещи");
    }
}