package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemRequestClient itemRequestClient;

    @Test
    void createRequest_shouldReturnCreated() throws Exception {
        ItemRequestDto requestDto = createValidItemRequestDto();
        when(itemRequestClient.createRequest(any(ItemRequestDto.class), anyLong()))
                .thenReturn(new ResponseEntity<>(requestDto, HttpStatus.CREATED));

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void createRequest_shouldReturnBadRequestWhenInvalidRequest() throws Exception {
        ItemRequestDto invalidRequestDto = new ItemRequestDto();

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createRequest_shouldReturnBadRequestWhenDescriptionIsTooLong() throws Exception {
        ItemRequestDto invalidRequestDto = new ItemRequestDto();
        String longDescription = "a".repeat(1001);
        invalidRequestDto.setDescription(longDescription);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getUserRequests_shouldReturnOk() throws Exception {
        List<Object> requests = List.of(createValidItemRequestDto());
        when(itemRequestClient.getUserRequests(anyLong()))
                .thenReturn(new ResponseEntity<>(requests, HttpStatus.OK));
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllRequests_shouldReturnOk() throws Exception {
        List<Object> requests = List.of(createValidItemRequestDto());
        when(itemRequestClient.getAllRequests(anyLong(), anyInt(), anyInt()))
                .thenReturn(new ResponseEntity<>(requests, HttpStatus.OK));

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllRequests_shouldUseDefaultPagination() throws Exception {
        List<Object> requests = List.of(createValidItemRequestDto());
        when(itemRequestClient.getAllRequests(anyLong(), anyInt(), anyInt()))
                .thenReturn(new ResponseEntity<>(requests, HttpStatus.OK));

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void getRequestById_shouldReturnOk() throws Exception {
        ItemRequestDto requestDto = createValidItemRequestDto();
        when(itemRequestClient.getRequestById(anyLong(), anyLong()))
                .thenReturn(new ResponseEntity<>(requestDto, HttpStatus.OK));

        mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void createRequest_shouldReturnBadRequestWhenDescriptionIsBlank() throws Exception {
        ItemRequestDto invalidRequestDto = new ItemRequestDto();
        invalidRequestDto.setDescription(""); // Blank description

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest());
    }

    private ItemRequestDto createValidItemRequestDto() {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription("Need a drill for home repairs");
        return requestDto;
    }
}