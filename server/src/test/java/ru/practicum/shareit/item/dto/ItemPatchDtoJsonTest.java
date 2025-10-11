package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemPatchDtoJsonTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldSerializeItemPatchDto() throws JsonProcessingException {
        ItemPatchDto patchDto = new ItemPatchDto();
        patchDto.setName(Optional.of("Updated Name"));
        patchDto.setDescription(Optional.of("Updated Description"));
        patchDto.setAvailable(Optional.of(true));

        String json = objectMapper.writeValueAsString(patchDto);

        assertThat(json).contains("\"name\":\"Updated Name\"");
        assertThat(json).contains("\"description\":\"Updated Description\"");
        assertThat(json).contains("\"available\":true");
    }

    @Test
    void shouldDeserializeItemPatchDto() throws JsonProcessingException {
        String json = "{\"name\":\"Updated Name\",\"description\":\"Updated Description\",\"available\":true}";

        ItemPatchDto patchDto = objectMapper.readValue(json, ItemPatchDto.class);

        assertThat(patchDto.getName()).isPresent().contains("Updated Name");
        assertThat(patchDto.getDescription()).isPresent().contains("Updated Description");
        assertThat(patchDto.getAvailable()).isPresent().contains(true);
    }

    @Test
    void shouldHandleNullValuesInItemPatchDto() throws JsonProcessingException {
        String json = "{}";

        ItemPatchDto patchDto = objectMapper.readValue(json, ItemPatchDto.class);

        assertThat(patchDto.getName()).isNotPresent();
        assertThat(patchDto.getDescription()).isNotPresent();
        assertThat(patchDto.getAvailable()).isNotPresent();
    }
}