package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private User owner;
    private Item item1;
    private Item item2;

    @BeforeEach
    void setUp() {
        itemRepository.deleteAll();
        userRepository.deleteAll();

        owner = new User();
        owner.setName("Owner");
        owner.setEmail("owner@mail.com");
        owner = userRepository.save(owner);

        item1 = new Item();
        item1.setName("Drill");
        item1.setDescription("Powerful electric drill");
        item1.setAvailable(true);
        item1.setOwner(owner);
        item1.setRequestId(1L);
        item1 = itemRepository.save(item1);

        item2 = new Item();
        item2.setName("Hammer");
        item2.setDescription("Heavy hammer for construction");
        item2.setAvailable(false);
        item2.setOwner(owner);
        item2.setRequestId(2L);
        item2 = itemRepository.save(item2);
    }

    @Test
    void save_ShouldSaveItem() {
        Item newItem = new Item();
        newItem.setName("New Item");
        newItem.setDescription("New Description");
        newItem.setAvailable(true);
        newItem.setOwner(owner);

        Item saved = itemRepository.save(newItem);

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals("New Item", saved.getName());
        assertEquals("New Description", saved.getDescription());
        assertTrue(saved.isAvailable());
        assertEquals(owner, saved.getOwner());
    }

    @Test
    void findByOwnerId_ShouldReturnOwnerItems() {
        Collection<Item> result = itemRepository.findByOwnerId(owner.getId());

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(item -> item.getName().equals("Drill")));
        assertTrue(result.stream().anyMatch(item -> item.getName().equals("Hammer")));
    }

    @Test
    void findByOwnerId_WhenNoItems_ShouldReturnEmptyCollection() {
        User newUser = new User();
        newUser.setName("New User");
        newUser.setEmail("new@mail.com");
        newUser = userRepository.save(newUser);

        Collection<Item> result = itemRepository.findByOwnerId(newUser.getId());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void deleteByIdAndOwnerId_ShouldDeleteItem() {
        itemRepository.deleteByIdAndOwnerId(item1.getId(), owner.getId());

        Optional<Item> deleted = itemRepository.findById(item1.getId());
        assertFalse(deleted.isPresent());
    }

    @Test
    void deleteByIdAndOwnerId_WhenWrongOwner_ShouldNotDelete() {
        User otherUser = new User();
        otherUser.setName("Other User");
        otherUser.setEmail("other@mail.com");
        otherUser = userRepository.save(otherUser);

        itemRepository.deleteByIdAndOwnerId(item1.getId(), otherUser.getId());

        Optional<Item> notDeleted = itemRepository.findById(item1.getId());
        assertTrue(notDeleted.isPresent());
    }

    @Test
    void findById_ShouldReturnItem() {
        Optional<Item> result = itemRepository.findById(item1.getId());

        assertTrue(result.isPresent());
        assertEquals("Drill", result.get().getName());
        assertEquals("Powerful electric drill", result.get().getDescription());
    }

    @Test
    void findById_WhenNotExists_ShouldReturnEmpty() {
        Optional<Item> result = itemRepository.findById(999L);

        assertFalse(result.isPresent());
    }

    @Test
    void searchAvailableItems_WhenTextInName_ShouldReturnItems() {
        Collection<Item> result = itemRepository.searchAvailableItems("drill");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Drill", result.iterator().next().getName());
    }

    @Test
    void searchAvailableItems_WhenTextInDescription_ShouldReturnItems() {
        Collection<Item> result = itemRepository.searchAvailableItems("electric");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Drill", result.iterator().next().getName());
    }

    @Test
    void searchAvailableItems_WhenItemNotAvailable_ShouldNotReturn() {
        Collection<Item> result = itemRepository.searchAvailableItems("hammer");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void searchAvailableItems_WhenNoMatches_ShouldReturnEmpty() {
        Collection<Item> result = itemRepository.searchAvailableItems("nonexistent");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void searchAvailableItems_WhenBlankText_ShouldReturnEmpty() {
        Collection<Item> result = itemRepository.searchAvailableItems("");

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void searchAvailableItems_ShouldBeCaseInsensitive() {
        Collection<Item> result = itemRepository.searchAvailableItems("DRILL");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Drill", result.iterator().next().getName());
    }

    @Test
    void findByRequestId_ShouldReturnItems() {
        List<Item> result = itemRepository.findByRequestId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Drill", result.get(0).getName());
    }

    @Test
    void findByRequestId_WhenNoItems_ShouldReturnEmptyList() {
        List<Item> result = itemRepository.findByRequestId(999L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findByRequestId_WithMultipleItems_ShouldReturnAll() {
        Item item3 = new Item();
        item3.setName("Another Drill");
        item3.setDescription("Another electric drill");
        item3.setAvailable(true);
        item3.setOwner(owner);
        item3.setRequestId(1L);
        itemRepository.save(item3);

        List<Item> result = itemRepository.findByRequestId(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(item -> item.getRequestId() == 1L));
    }
}