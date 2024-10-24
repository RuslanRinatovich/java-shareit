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
import java.util.Optional;

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
        Optional<User> user = userService.getUser(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("user with id = " + userId + " didn't find");
        }
        Optional<Item> item = itemService.getItem(newBookingDto.getItemId());
        if (item.isEmpty()) {
            throw new NotFoundException("item with id = " + newBookingDto.getItemId() + " didn't find");
        }
        if (!item.get().getAvailable()) {
            throw new ValidationException("item with id = " + newBookingDto.getItemId() + " unavailable");
        }

        Booking booking = new Booking();
        booking.setStatus(Status.WAITING);
        booking.setEnd(newBookingDto.getEnd());
        booking.setStart(newBookingDto.getStart());
        booking.setItem(item.get());
        booking.setBooker(user.get());
        userService.getUser(booking.getBooker().getId());
        final Booking createdBooking = repository.save(booking);
        log.info("created booking with id = {}: {}", createdBooking.getId(), createdBooking);
        return createdBooking;
    }

    @Override
    public Booking getBooking(final Long id, final Long userId) {
        Optional<User> user = userService.getUser(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("user with id = " + userId + " didn't find");
        }
        return repository.findByIdAndBookerIdOrIdAndItemOwnerId(id, userId, id, userId).orElseThrow(
                () -> new NotFoundException("booking didn't find")
        );
    }

    @Override
    public List<Booking> getBookerBookings(final long userId, final String status, final int from, final int size) {
        final Sort sort = Sort.by(Sort.Direction.DESC, "start");
        final Pageable page = PageRequest.of(from / size, size, sort);
        BookingStatusFilter filter = BookingStatusFilter.valueOf(status);
        switch (filter) {
            case ALL:
                return repository.findByBookerId(userId, page);
            case CURRENT:
                return repository.findCurrentBookingsForBooker(userId, page);
            case PAST:
                return repository.findPastBookingsForBooker(userId, page);
            case FUTURE:
                return repository.findFutureBookingsForBooker(userId, page);
            case WAITING:
                return repository.findBookingsForBookerWithStatus(userId, Status.WAITING, page);
            case REJECTED:
                return repository.findBookingsForBookerWithStatus(userId, Status.REJECTED, page);
            case null:
                throw new AssertionError();
        }
    }

    @Override
    public List<Booking> getOwnerBookings(final long userId, final String status, final int from, final int size) {

        if (!itemService.existByOwnerId(userId)) {
            throw new ForbiddenException("your are not owner the item");
        }
        final Sort sort = Sort.by(Sort.Direction.DESC, "start");
        final Pageable page = PageRequest.of(from / size, size, sort);
        BookingStatusFilter filter = BookingStatusFilter.valueOf(status);
        switch (filter) {
            case ALL:
                return repository.findByItemOwnerId(userId, page);
            case CURRENT:
                return repository.findCurrentBookingsForOwner(userId, page);
            case PAST:
                return repository.findPastBookingsForOwner(userId, page);
            case FUTURE:
                return repository.findFutureBookingsForOwner(userId, page);
            case WAITING:
                return repository.findBookingsForOwnerWithStatus(userId, Status.WAITING, page);
            case REJECTED:
                return repository.findBookingsForOwnerWithStatus(userId, Status.REJECTED, page);
            case null:
                throw new AssertionError();
        }
    }

    @Override
    @Transactional
    public Booking changeBookingStatus(final long id, final boolean isApproved, final long userId) {
        Optional<Booking> booking = repository.findByIdAndItemOwnerId(id, userId);

        if (booking.isEmpty()) {
            throw new ForbiddenException("bookings for user with id = " + userId + " didn't find");
        }
        if (!userService.existsById(userId)) {
            throw new ForbiddenException("your are not owner the item");
        }

        if (!Status.WAITING.equals(booking.get().getStatus())) {
            throw new ValidationException("booking should be have status " + Status.WAITING);
        }
        Booking updated = booking.get();
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
