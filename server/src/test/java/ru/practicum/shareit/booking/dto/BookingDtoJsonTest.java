package ru.practicum.shareit.booking.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@TestPropertySource(locations = "classpath:test.properties")
class BookingDtoJsonTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldSerializeBookingDto() throws Exception {
        // Given
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        LocalDateTime start = LocalDateTime.of(2023, 12, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2023, 12, 2, 10, 0);
        bookingDto.setStart(start);
        bookingDto.setEnd(end);

        // When
        String json = objectMapper.writeValueAsString(bookingDto);

        // Then
        assertThat(json).contains("\"itemId\":1");
        assertThat(json).contains("\"start\":\"2023-12-01T10:00:00\"");
        assertThat(json).contains("\"end\":\"2023-12-02T10:00:00\"");
    }

    @Test
    void shouldDeserializeBookingDto() throws Exception {
        // Given
        String json = "{\"itemId\":1,\"start\":\"2023-12-01T10:00:00\",\"end\":\"2023-12-02T10:00:00\"}";

        // When
        BookingDto bookingDto = objectMapper.readValue(json, BookingDto.class);

        // Then
        assertThat(bookingDto.getItemId()).isEqualTo(1L);
        assertThat(bookingDto.getStart()).isEqualTo(LocalDateTime.of(2023, 12, 1, 10, 0));
        assertThat(bookingDto.getEnd()).isEqualTo(LocalDateTime.of(2023, 12, 2, 10, 0));
    }

    @Test
    void shouldHandleNullValues() throws Exception {
        // Given
        BookingDto bookingDto = new BookingDto();

        // When
        String json = objectMapper.writeValueAsString(bookingDto);

        // Then
        assertThat(json).contains("\"itemId\":null");
        assertThat(json).contains("\"start\":null");
        assertThat(json).contains("\"end\":null");
    }
}