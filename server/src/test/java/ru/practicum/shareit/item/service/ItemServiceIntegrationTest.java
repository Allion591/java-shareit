package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.comment.repository.CommentsRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ItemServiceIntegrationTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentsRepository commentsRepository;

    private User owner;
    private User booker;
    private Item item;

    @BeforeEach
    void setUp() {
        commentsRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();

        owner = new User();
        owner.setName("Owner");
        owner.setEmail("owner@mail.com");
        owner = userRepository.save(owner);

        booker = new User();
        booker.setName("Booker");
        booker.setEmail("booker@mail.com");
        booker = userRepository.save(booker);

        item = new Item();
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);
        item.setOwner(owner);
        item = itemRepository.save(item);
    }

    @Test
    void create_IntegrationTest() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("New Item");
        itemDto.setDescription("New Description");
        itemDto.setAvailable(true);

        ItemResponseDto result = itemService.create(itemDto, owner.getId());

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("New Item", result.getName());
        assertEquals("New Description", result.getDescription());
        assertTrue(result.isAvailable());

        Optional<Item> savedItem = itemRepository.findById(result.getId());
        assertTrue(savedItem.isPresent());
        assertEquals("New Item", savedItem.get().getName());
    }

    @Test
    void update_IntegrationTest() {
        ItemPatchDto patchDto = new ItemPatchDto();
        patchDto.setName(Optional.of("Updated Item"));
        patchDto.setDescription(Optional.of("Updated Description"));

        ItemResponseDto result = itemService.update(patchDto, item.getId(), owner.getId());

        assertNotNull(result);
        assertEquals("Updated Item", result.getName());
        assertEquals("Updated Description", result.getDescription());

        Optional<Item> updatedItem = itemRepository.findById(item.getId());
        assertTrue(updatedItem.isPresent());
        assertEquals("Updated Item", updatedItem.get().getName());
    }

    @Test
    void getById_IntegrationTest() {
        ItemResponseDto result = itemService.getById(item.getId(), owner.getId());

        assertNotNull(result);
        assertEquals(item.getId(), result.getId());
        assertEquals("Test Item", result.getName());
        assertEquals("Test Description", result.getDescription());
    }

    @Test
    void search_IntegrationTest() {
        Collection<ItemResponseDto> result = itemService.search("Test", owner.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.stream().anyMatch(item -> item.getName().contains("Test")));
    }

    @Test
    void getItems_IntegrationTest() {
        Collection<ItemResponseDto> result = itemService.getItems(owner.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(item.getId(), result.iterator().next().getId());
    }
}