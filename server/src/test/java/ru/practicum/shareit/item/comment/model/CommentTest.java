package ru.practicum.shareit.item.comment.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CommentTest {

    @Test
    void commentCreation_ShouldSetFieldsCorrectly() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Test comment");

        User author = new User();
        author.setId(1L);

        Item item = new Item();
        item.setId(1L);

        comment.setAuthor(author);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());

        assertEquals(1L, comment.getId());
        assertEquals("Test comment", comment.getText());
        assertEquals(author, comment.getAuthor());
        assertEquals(item, comment.getItem());
        assertNotNull(comment.getCreated());
    }

    @Test
    void commentEqualsAndHashCode_ShouldWorkCorrectly() {
        Comment comment1 = new Comment();
        comment1.setId(1L);

        Comment comment2 = new Comment();
        comment2.setId(1L);

        Comment comment3 = new Comment();
        comment3.setId(2L);

        assertEquals(comment1, comment2);
        assertEquals(comment1.hashCode(), comment2.hashCode());
        assertNotEquals(comment1, comment3);
    }

    @Test
    void commentNoArgsConstructor_ShouldCreateEmptyComment() {
        Comment comment = new Comment();

        assertNotNull(comment);
        assertNull(comment.getId());
        assertNull(comment.getText());
        assertNull(comment.getAuthor());
        assertNull(comment.getItem());
        assertNull(comment.getCreated());
    }

    @Test
    void commentEquals_WithSameObject_ShouldReturnTrue() {
        Comment comment = new Comment();
        comment.setId(1L);

        assertEquals(comment, comment);
    }

    @Test
    void commentEquals_WithNull_ShouldReturnFalse() {
        Comment comment = new Comment();
        comment.setId(1L);

        assertNotEquals(null, comment);
    }

    @Test
    void commentEquals_WithDifferentClass_ShouldReturnFalse() {
        Comment comment = new Comment();
        comment.setId(1L);

        assertNotEquals("string", comment);
    }
}