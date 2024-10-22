package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Collection<Booking> findAllByBooker(User user);
    Optional<Booking> findByIdAndBookerIdOrIdAndItemOwnerId(Long id, Long bookerId, Long id1, Long itemOwnerId);

    Collection<Booking> findByBookerId(Long bookerId, Pageable page);

    Collection<Booking> findByBookerIdStartLessThenCurrentDateAndEndGreaterThanEqualCurrentDate(Long bookerId, LocalDateTime currentDate, Pageable page);


}
