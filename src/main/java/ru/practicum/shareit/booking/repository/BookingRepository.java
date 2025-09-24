package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId, Pageable pageable);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Long bookerId, BookingStatus status,
                                                          Pageable pageable);

    List<Booking> findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
            Long bookerId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    List<Booking> findByBookerIdAndEndBeforeOrderByStartDesc(Long bookerId, LocalDateTime end,
                                                             Pageable pageable);

    List<Booking> findByBookerIdAndStartAfterOrderByStartDesc(Long bookerId, LocalDateTime start,
                                                              Pageable pageable);

    List<Booking> findByItemOwnerIdOrderByStartDesc(Long ownerId, Pageable pageable);

    List<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(Long ownerId, BookingStatus status,
                                                             Pageable pageable);

    List<Booking> findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(
            Long ownerId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    List<Booking> findByItemOwnerIdAndEndBeforeOrderByStartDesc(Long ownerId, LocalDateTime end,
                                                                Pageable pageable);

    List<Booking> findByItemOwnerIdAndStartAfterOrderByStartDesc(Long ownerId, LocalDateTime start,
                                                                 Pageable pageable);

    boolean existsByItemIdAndStartLessThanEqualAndEndGreaterThanEqualAndStatusIn(
            Long itemId, LocalDateTime start, LocalDateTime end, Collection<BookingStatus> statuses);

    Optional<Booking> findFirstByItemIdAndStatusAndStartLessThanEqualOrderByStartDesc(
            Long itemId, BookingStatus status, LocalDateTime date);

    Optional<Booking> findFirstByItemIdAndStatusAndStartAfterOrderByStartAsc(
            Long itemId, BookingStatus status, LocalDateTime date);

    @Query("SELECT COUNT(b) > 0 FROM Booking b " +
            "WHERE b.booker.id = :userId " +
            "AND b.item.id = :itemId " +
            "AND b.status = :status " +
            "AND b.end < :now")
    boolean existsCompletedBookingByUserAndItem(
            @Param("userId") Long userId,
            @Param("itemId") Long itemId,
            @Param("status") BookingStatus status,
            @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND b.status = :status AND b.start <= :now " +
            "ORDER BY b.start DESC")
    Optional<Booking> findLastBooking(@Param("itemId") Long itemId, @Param("status") BookingStatus status,
                                      @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND b.status = :status AND b.start > :now " +
            "ORDER BY b.start ASC")
    Optional<Booking> findNextBooking(@Param("itemId") Long itemId, @Param("status") BookingStatus status,
                                      @Param("now") LocalDateTime now);
}