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
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Validated
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
class BookingServiceImpl implements BookingService {

    private final BookingRepository repository;
    private final UserService userService;
    private final ItemService itemService;
    @Override
    @Transactional
    public Booking create(final NewBookingDto newBookingDto, Long userId) {
        Objects.requireNonNull(newBookingDto, "Cannot create booking: is null");
        Objects.requireNonNull(newBookingDto.getItemId(), "Cannot create booking: itemId is null");
        Objects.requireNonNull(userId, "Cannot create booking: booker is null");
        if (!newBookingDto.getEnd().isAfter(newBookingDto.getStart())) {
            throw new ValidationException("end should be after start");
        }
        Optional<User> user = userService.getUser(userId);
        if (user.isEmpty())
        {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        Optional<Item> item = itemService.getItem(newBookingDto.getItemId());
        if(item.isEmpty())
        {
            throw new NotFoundException("Item с id = " + newBookingDto.getItemId() + " не найден");
        }
        if(!item.get().getAvailable())
        {
            throw new ValidationException("Item не доступен");
        }

        Booking booking = new Booking();
        booking.setStatus(Status.WAITING);
        booking.setEnd(newBookingDto.getEnd());
        booking.setStart(newBookingDto.getStart());
        booking.setItem(item.get());
        booking.setBooker(user.get());
        userService.getUser(booking.getBooker().getId());
        final Booking createdBooking = repository.save(booking);
        log.info("Created booking with id = {}: {}", createdBooking.getId(), createdBooking);
        return createdBooking;
    }

    @Override
    public Booking getBooking(final Long id, final Long userId) {
        Optional<User> user = userService.getUser(userId);
        if (user.isEmpty())
        {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        return repository.findByIdAndBookerIdOrIdAndItemOwnerId(id, userId, id, userId).orElseThrow(
                () -> new NotFoundException("Бронь не найдена")
        );
    }
    @Override
    public List<Booking> getBookerBookings(final long userId, final String status, final int from,  final int size ) {
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
    public List<Booking> getOwnerBookings(final long userId, final String status, final int from,  final int size ) {

        if (!itemService.existByOwnerId(userId)) {
            throw new NotFoundException("Вы не являетесь владельцем ни одного из товаров");
        }
        final Sort sort = Sort.by(Sort.Direction.DESC, "start");
        final Pageable page = PageRequest.of(from / size, size, sort);
        BookingStatusFilter filter = BookingStatusFilter.valueOf(status);
//        switch (filter) {
//            case ALL:
//                return repository.findByItemOwnerId(userId, page);
//            case CURRENT:
//                return repository.findCurrentBookingsForOwner(userId, page);
//            case PAST:
//                return repository.findPastBookingsForOwner(userId, page);
//            case FUTURE:
//                return repository.findFutureBookingsForOwner(userId, page);
//            case WAITING:
//                return repository.findBookingsForOwnerWithStatus(userId, Status.WAITING, page);
//            case REJECTED:
//                return repository.findBookingsForOwnerWithStatus(userId, Status.REJECTED, page);
//            case null:
//                throw new AssertionError();
//        }
        return null;
    }





}
