package ru.practicum.shareit.booking;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.List;
import java.util.Objects;

@Service
@Validated
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository repository;
    private final UserService userService;
    private final ItemService itemService;

    @Override
    @Transactional
    public Booking create(final NewBookingDto newBookingDto, Long userId) {
        Objects.requireNonNull(newBookingDto, "Cannot create booking: booking is null");
        Objects.requireNonNull(newBookingDto.getItemId(), "Cannot create booking: itemId is null");
        Objects.requireNonNull(userId, "Cannot create booking: booker is null");
        if (!newBookingDto.getEnd().isAfter(newBookingDto.getStart())) {
            throw new ValidationException("end should be after start");
        }
        User user = userService.getUser(userId)
                .orElseThrow(() -> new NotFoundException("user with id = " + userId + " didn't find"));

        Item item = itemService.getItem(newBookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("item with id = " + newBookingDto.getItemId() + " didn't find"));
        if (!item.getAvailable()) {
            throw new ValidationException("item with id = " + newBookingDto.getItemId() + " unavailable");
        }

        Booking booking = new Booking();
        booking.setStatus(Status.WAITING);
        booking.setEnd(newBookingDto.getEnd());
        booking.setStart(newBookingDto.getStart());
        booking.setItem(item);
        booking.setBooker(user);
        userService.getUser(booking.getBooker().getId());
        final Booking createdBooking = repository.save(booking);
        log.info("created booking with id = {}: {}", createdBooking.getId(), createdBooking);
        return createdBooking;
    }

    @Override
    public Booking getBooking(final Long id, final Long userId) {
        User user = userService.getUser(userId)
                .orElseThrow(() -> new NotFoundException("user with id = " + userId + " didn't find"));
        return repository.findByIdAndBookerIdOrIdAndItemOwnerId(id, userId, id, userId).orElseThrow(
                () -> new NotFoundException("booking didn't find")
        );
    }

    @Override
    public List<Booking> getBookerBookings(final long userId, final String status, final int from, final int size) {
        final Sort sort = Sort.by(Sort.Direction.DESC, "start");
        final Pageable page = PageRequest.of(from / size, size, sort);
        BookingStatusFilter filter = BookingStatusFilter.valueOf(status);
        return switch (filter) {
            case ALL -> repository.findByBookerId(userId, page);
            case CURRENT -> repository.findCurrentBookingsForBooker(userId, page);
            case PAST -> repository.findPastBookingsForBooker(userId, page);
            case FUTURE -> repository.findFutureBookingsForBooker(userId, page);
            case WAITING -> repository.findBookingsForBookerWithStatus(userId, Status.WAITING, page);
            case REJECTED -> repository.findBookingsForBookerWithStatus(userId, Status.REJECTED, page);
        };
    }

    @Override
    public List<Booking> getOwnerBookings(final long userId, final String status, final int from, final int size) {

        if (!itemService.existByOwnerId(userId)) {
            throw new ForbiddenException("your are not owner the item");
        }
        final Sort sort = Sort.by(Sort.Direction.DESC, "start");
        final Pageable page = PageRequest.of(from / size, size, sort);
        BookingStatusFilter filter = BookingStatusFilter.valueOf(status);
        return switch (filter) {
            case ALL -> repository.findByItemOwnerId(userId, page);
            case CURRENT -> repository.findCurrentBookingsForOwner(userId, page);
            case PAST -> repository.findPastBookingsForOwner(userId, page);
            case FUTURE -> repository.findFutureBookingsForOwner(userId, page);
            case WAITING -> repository.findBookingsForOwnerWithStatus(userId, Status.WAITING, page);
            case REJECTED -> repository.findBookingsForOwnerWithStatus(userId, Status.REJECTED, page);
        };
    }

    @Override
    @Transactional
    public Booking changeBookingStatus(final long id, final boolean isApproved, final long userId) {
        Booking updated = repository.findByIdAndItemOwnerId(id, userId)
                .orElseThrow(() -> new ForbiddenException("bookings for user with id = " + userId + " didn't find"));
        if (!userService.existsById(userId)) {
            throw new ForbiddenException("your are not owner the item");
        }
        if (!Status.WAITING.equals(updated.getStatus())) {
            throw new ValidationException("booking should be have status " + Status.WAITING);
        }

        updated.setStatus(isApproved ? Status.APPROVED : Status.REJECTED);
        final Booking updatedBooking = repository.save(updated);
        log.info("Changed status of booking id = {} to {}", id, updatedBooking.getStatus());
        return updatedBooking;
    }

    @Override
    public List<Booking> findCompletedBookingForUserAndItem(long userId, long itemId) {
        return repository.findCompletedBookingForUserAndItem(userId, itemId);
    }
}
