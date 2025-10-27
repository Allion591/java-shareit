package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemClient itemClient;

    @Test
    void add_shouldReturnCreated() throws Exception {
        ItemDto itemDto = createValidItemDto();
        when(itemClient.saveNewItem(any(ItemDto.class), anyLong()))
                .thenReturn(new ResponseEntity<>(itemDto, HttpStatus.CREATED));

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void add_shouldReturnBadRequestWhenInvalidItem() throws Exception {
        ItemDto invalidItemDto = new ItemDto();

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidItemDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_shouldReturnOk() throws Exception {
        ItemPatchDto itemPatchDto = new ItemPatchDto();
        itemPatchDto.setName("Updated Name");
        when(itemClient.update(any(ItemPatchDto.class), anyLong(), anyLong()))
                .thenReturn(new ResponseEntity<>(itemPatchDto, HttpStatus.OK));

        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemPatchDto)))
                .andExpect(status().isOk());
    }

    @Test
    void getById_shouldReturnOk() throws Exception {
        when(itemClient.getById(anyLong(), anyLong()))
                .thenReturn(new ResponseEntity<>("{}", HttpStatus.OK));

        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void get_shouldReturnOk() throws Exception {
        when(itemClient.getItems(anyLong()))
                .thenReturn(new ResponseEntity<>(List.of(), HttpStatus.OK));

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void search_shouldReturnOk() throws Exception {
        when(itemClient.search(anyString(), anyLong()))
                .thenReturn(new ResponseEntity<>(List.of(), HttpStatus.OK));

        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1L)
                        .param("text", "test"))
                .andExpect(status().isOk());
    }

    @Test
    void addComment_shouldReturnCreated() throws Exception {
        CommentDto commentDto = new CommentDto();
        commentDto.setText("Great item!");
        when(itemClient.saveComment(anyLong(), anyLong(), any(CommentDto.class)))
                .thenReturn(new ResponseEntity<>(commentDto, HttpStatus.CREATED));

        mockMvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void addComment_shouldReturnBadRequestWhenInvalidComment() throws Exception {
        CommentDto invalidCommentDto = new CommentDto(); // Missing text

        mockMvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCommentDto)))
                .andExpect(status().isBadRequest());
    }

    private ItemDto createValidItemDto() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Test Item");
        itemDto.setDescription("Test Description");
        itemDto.setAvailable(true);
        return itemDto;
    }
}