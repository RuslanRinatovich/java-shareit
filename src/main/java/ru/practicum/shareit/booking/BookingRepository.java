package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Collection<Booking> findAllByBooker(User user);
    List<Booking> findByItemOwnerId(Long bookerId, Pageable page);

    Optional<Booking> findByIdAndItemOwnerId(Long id, Long ownerId);
    Optional<Booking> findByIdAndBookerIdOrIdAndItemOwnerId(Long id, Long bookerId, Long id1, Long itemOwnerId);

    List<Booking> findByBookerId(Long bookerId, Pageable page);

    @Query("select b from Booking b join fetch b.booker join fetch b.item where b.booker.id = :userId "
            + "and b.start <= current_timestamp and b.end > current_timestamp")
    List<Booking> findCurrentBookingsForBooker(@Param("userId") long userId, Pageable page);

    @Query("select b from Booking b join fetch b.booker join fetch b.item where b.booker.id = :userId "
            + "and b.end <= current_timestamp")
    List<Booking> findPastBookingsForBooker(@Param("userId") long userId, Pageable page);

    @Query("select b from Booking b join fetch b.booker join fetch b.item where b.booker.id = :userId "
            + "and b.start > current_timestamp")
    List<Booking> findFutureBookingsForBooker(@Param("userId") long userId, Pageable page);

    @Query("select b from Booking b join fetch b.booker join fetch b.item where b.booker.id = :userId "
            + "and b.status = :status")
    List<Booking> findBookingsForBookerWithStatus(@Param("userId") long userId, @Param("status") Status status, Pageable page);




    @Query("select b from Booking b join fetch b.booker join fetch b.item join b.item.owner "
            + "where b.item.owner.id = :userId and b.start <= current_timestamp and b.end > current_timestamp")
    List<Booking> findCurrentBookingsForOwner(@Param("userId") long userId, Pageable page);

    @Query("select b from Booking b join fetch b.booker join fetch b.item join b.item.owner "
            + "where b.item.owner.id = :userId and b.end <= current_timestamp")
    List<Booking> findPastBookingsForOwner(@Param("userId") long userId, Pageable page);

    @Query("select b from Booking b join fetch b.booker join fetch b.item join b.item.owner "
            + "where b.item.owner.id = :userId and b.start > current_timestamp")
    List<Booking> findFutureBookingsForOwner(@Param("userId") long userId, Pageable page);

    @Query("select b from Booking b join fetch b.booker join fetch b.item join b.item.owner "
            + "where b.item.owner.id = :userId and b.status = :status")
    List<Booking> findBookingsForOwnerWithStatus(@Param("userId") long userId, @Param("status") Status status, Pageable page);



}
