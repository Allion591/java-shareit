package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.item.dto.ItemResponseDtoShort;

import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestResponseDtoJsonTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldSerializeItemRequestResponseDto() throws JsonProcessingException {
        ItemResponseDtoShort itemDto = new ItemResponseDtoShort();
        itemDto.setId(1L);
        itemDto.setName("Drill");
        itemDto.setOwner(1L);

        ItemRequestResponseDto responseDto = new ItemRequestResponseDto();
        responseDto.setId(1L);
        responseDto.setDescription("Need a drill");
        responseDto.setRequester(1L);
        responseDto.setCreated(LocalDateTime.of(2023, 12, 1, 10, 0));
        responseDto.setItems(List.of(itemDto));

        String json = objectMapper.writeValueAsString(responseDto);

        assertThat(json).contains("\"id\":1");
        assertThat(json).contains("\"description\":\"Need a drill\"");
        assertThat(json).contains("\"requester\":1");
        assertThat(json).contains("\"created\":\"2023-12-01T10:00:00\"");
        assertThat(json).contains("\"items\"");
    }

    @Test
    void shouldDeserializeItemRequestResponseDto() throws JsonProcessingException {
        String json = "{\"id\":1,\"description\":\"Need a drill\",\"requester\":1,\"created\":\"2023-12-01T10:00:00\",\"items\":[]}";

        ItemRequestResponseDto responseDto = objectMapper.readValue(json, ItemRequestResponseDto.class);

        assertThat(responseDto.getId()).isEqualTo(1L);
        assertThat(responseDto.getDescription()).isEqualTo("Need a drill");
        assertThat(responseDto.getRequester()).isEqualTo(1L);
        assertThat(responseDto.getCreated()).isEqualTo(LocalDateTime.of(2023, 12, 1, 10, 0));
        assertThat(responseDto.getItems()).isEmpty();
    }
}