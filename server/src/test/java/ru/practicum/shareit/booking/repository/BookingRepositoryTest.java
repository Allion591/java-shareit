package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private User owner;
    private User booker;
    private Item item;
    private Booking currentBooking;
    private Booking pastBooking;
    private Booking futureBooking;
    private Booking waitingBooking;

    @BeforeEach
    void setUp() {
        bookingRepository.deleteAll();
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

        pastBooking = new Booking();
        pastBooking.setStart(LocalDateTime.now().minusDays(10));
        pastBooking.setEnd(LocalDateTime.now().minusDays(5));
        pastBooking.setItem(item);
        pastBooking.setBooker(booker);
        pastBooking.setStatus(BookingStatus.APPROVED);
        pastBooking = bookingRepository.save(pastBooking);

        currentBooking = new Booking();
        currentBooking.setStart(LocalDateTime.now().minusDays(1));
        currentBooking.setEnd(LocalDateTime.now().plusDays(1));
        currentBooking.setItem(item);
        currentBooking.setBooker(booker);
        currentBooking.setStatus(BookingStatus.APPROVED);
        currentBooking = bookingRepository.save(currentBooking);

        futureBooking = new Booking();
        futureBooking.setStart(LocalDateTime.now().plusDays(5));
        futureBooking.setEnd(LocalDateTime.now().plusDays(10));
        futureBooking.setItem(item);
        futureBooking.setBooker(booker);
        futureBooking.setStatus(BookingStatus.APPROVED);
        futureBooking = bookingRepository.save(futureBooking);

        waitingBooking = new Booking();
        waitingBooking.setStart(LocalDateTime.now().plusDays(15));
        waitingBooking.setEnd(LocalDateTime.now().plusDays(20));
        waitingBooking.setItem(item);
        waitingBooking.setBooker(booker);
        waitingBooking.setStatus(BookingStatus.WAITING);
        waitingBooking = bookingRepository.save(waitingBooking);
    }

    @Test
    void findByBookerIdOrderByStartDesc_ShouldReturnBookings() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("start").descending());

        List<Booking> result = bookingRepository.findByBookerIdOrderByStartDesc(booker.getId(), pageable);

        assertNotNull(result);
        assertEquals(4, result.size());
        assertTrue(result.get(0).getStart().isAfter(result.get(1).getStart()));
    }

    @Test
    void findByBookerIdAndStatusOrderByStartDesc_ShouldReturnFilteredBookings() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("start").descending());

        List<Booking> result = bookingRepository.findByBookerIdAndStatusOrderByStartDesc(
                booker.getId(), BookingStatus.WAITING, pageable);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(BookingStatus.WAITING, result.get(0).getStatus());
    }

    @Test
    void findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc_ShouldReturnCurrentBookings() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("start").descending());

        List<Booking> result = bookingRepository.findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                booker.getId(), LocalDateTime.now(), LocalDateTime.now(), pageable);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(currentBooking.getId(), result.get(0).getId());
    }

    @Test
    void findByBookerIdAndEndBeforeOrderByStartDesc_ShouldReturnPastBookings() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("start").descending());

        List<Booking> result = bookingRepository.findByBookerIdAndEndBeforeOrderByStartDesc(
                booker.getId(), LocalDateTime.now(), pageable);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(pastBooking.getId(), result.get(0).getId());
    }

    @Test
    void findByBookerIdAndStartAfterOrderByStartDesc_ShouldReturnFutureBookings() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("start").descending());

        List<Booking> result = bookingRepository.findByBookerIdAndStartAfterOrderByStartDesc(
                booker.getId(), LocalDateTime.now(), pageable);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(b -> b.getStart().isAfter(LocalDateTime.now())));
    }

    @Test
    void findByItemOwnerIdOrderByStartDesc_ShouldReturnOwnerBookings() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("start").descending());

        List<Booking> result = bookingRepository.findByItemOwnerIdOrderByStartDesc(owner.getId(), pageable);

        assertNotNull(result);
        assertEquals(4, result.size());
        assertTrue(result.get(0).getStart().isAfter(result.get(1).getStart()));
    }

    @Test
    void findByItemOwnerIdAndStatusOrderByStartDesc_ShouldReturnFilteredOwnerBookings() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("start").descending());

        List<Booking> result = bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(
                owner.getId(), BookingStatus.APPROVED, pageable);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.stream().allMatch(b -> b.getStatus() == BookingStatus.APPROVED));
    }

    @Test
    void findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc_ShouldReturnCurrentOwnerBookings() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("start").descending());

        List<Booking> result = bookingRepository.findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                owner.getId(), LocalDateTime.now(), LocalDateTime.now(), pageable);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(currentBooking.getId(), result.get(0).getId());
    }

    @Test
    void findByItemOwnerIdAndEndBeforeOrderByStartDesc_ShouldReturnPastOwnerBookings() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("start").descending());

        List<Booking> result = bookingRepository.findByItemOwnerIdAndEndBeforeOrderByStartDesc(
                owner.getId(), LocalDateTime.now(), pageable);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(pastBooking.getId(), result.get(0).getId());
    }

    @Test
    void findByItemOwnerIdAndStartAfterOrderByStartDesc_ShouldReturnFutureOwnerBookings() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("start").descending());

        List<Booking> result = bookingRepository.findByItemOwnerIdAndStartAfterOrderByStartDesc(
                owner.getId(), LocalDateTime.now(), pageable);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(b -> b.getStart().isAfter(LocalDateTime.now())));
    }

    @Test
    void existsOverlappingBooking_WhenOverlapping_ShouldReturnTrue() {
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(8);
        Collection<BookingStatus> statuses = List.of(BookingStatus.APPROVED, BookingStatus.WAITING);

        boolean result = bookingRepository.existsOverlappingBooking(
                item.getId(), start, end, statuses);

        assertTrue(result);
    }

    @Test
    void existsOverlappingBooking_WhenNotOverlapping_ShouldReturnFalse() {
        LocalDateTime start = LocalDateTime.now().plusDays(25);
        LocalDateTime end = LocalDateTime.now().plusDays(30);
        Collection<BookingStatus> statuses = List.of(BookingStatus.APPROVED, BookingStatus.WAITING);

        boolean result = bookingRepository.existsOverlappingBooking(
                item.getId(), start, end, statuses);

        assertFalse(result);
    }

    @Test
    void existsOverlappingBooking_WithDifferentStatuses_ShouldFilterCorrectly() {
        LocalDateTime start = LocalDateTime.now().plusDays(16);
        LocalDateTime end = LocalDateTime.now().plusDays(18);
        Collection<BookingStatus> onlyApproved = List.of(BookingStatus.APPROVED);

        boolean result = bookingRepository.existsOverlappingBooking(
                item.getId(), start, end, onlyApproved);

        assertFalse(result);
    }

    @Test
    void existsCompletedBookingByUserAndItem_WhenExists_ShouldReturnTrue() {
        boolean result = bookingRepository.existsCompletedBookingByUserAndItem(
                booker.getId(), item.getId(), BookingStatus.APPROVED, LocalDateTime.now());

        assertTrue(result);
    }

    @Test
    void existsCompletedBookingByUserAndItem_WhenNotExists_ShouldReturnFalse() {
        User otherUser = new User();
        otherUser.setName("Other User");
        otherUser.setEmail("other@mail.com");
        otherUser = userRepository.save(otherUser);

        boolean result = bookingRepository.existsCompletedBookingByUserAndItem(
                otherUser.getId(), item.getId(), BookingStatus.APPROVED, LocalDateTime.now());

        assertFalse(result);
    }

    @Test
    void findLastBooking_WhenNoPastBookings_ShouldReturnEmpty() {
        bookingRepository.deleteAll();

        Optional<Booking> result = bookingRepository.findLastBooking(
                item.getId(), BookingStatus.APPROVED, LocalDateTime.now());

        assertFalse(result.isPresent());
    }

    @Test
    void findNextBooking_ShouldReturnNextBooking() {
        Optional<Booking> result = bookingRepository.findNextBooking(
                item.getId(), BookingStatus.APPROVED, LocalDateTime.now());

        assertTrue(result.isPresent());
        assertEquals(futureBooking.getId(), result.get().getId());
    }

    @Test
    void findNextBooking_WhenNoFutureBookings_ShouldReturnEmpty() {
        bookingRepository.deleteAll();
        Booking past = new Booking();
        past.setStart(LocalDateTime.now().minusDays(10));
        past.setEnd(LocalDateTime.now().minusDays(5));
        past.setItem(item);
        past.setBooker(booker);
        past.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(past);


        Optional<Booking> result = bookingRepository.findNextBooking(
                item.getId(), BookingStatus.APPROVED, LocalDateTime.now());

        assertFalse(result.isPresent());
    }

    @Test
    void findNextBooking_ShouldIgnoreWaitingStatus() {
        Optional<Booking> result = bookingRepository.findNextBooking(
                item.getId(), BookingStatus.APPROVED, LocalDateTime.now());

        assertTrue(result.isPresent());
        assertEquals(futureBooking.getId(), result.get().getId());
        assertNotEquals(waitingBooking.getId(), result.get().getId());
    }

    @Test
    void pagination_ShouldWorkCorrectly() {
        Pageable firstPage = PageRequest.of(0, 2, Sort.by("start").descending());

        List<Booking> result = bookingRepository.findByBookerIdOrderByStartDesc(booker.getId(), firstPage);

        assertNotNull(result);
        assertEquals(2, result.size());
    }
}