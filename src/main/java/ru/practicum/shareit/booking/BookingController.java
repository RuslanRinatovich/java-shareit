package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.util.Marker;

import java.util.Collection;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @Validated({Marker.OnCreate.class})
    public BookingDto createBooking(
            @RequestHeader("X-Sharer-User-Id") final Long userId,
            @RequestBody @Valid final NewBookingDto newBookingDto
    ) {
        log.info("Received POST at /bookings");
        return BookingMapper.mapToDto(bookingService.create(newBookingDto, userId));
    }


    @GetMapping("/{id}")
    public BookingDto getBooking(
            @RequestHeader("X-Sharer-User-Id") final Long userId,
            @PathVariable final long id

    ) {
        log.info("Get booking bookingId={}", id);
        return BookingMapper.mapToDto(bookingService.getBooking(id, userId));
    }

    @GetMapping
    public Collection<BookingDto> getUserBookings(
            @RequestHeader("X-Sharer-User-Id") final long userId,
            @RequestParam(defaultValue = "ALL") final String state,
            @RequestParam(defaultValue = "0") @PositiveOrZero final int from,
            @RequestParam(defaultValue = "10") @Positive final int size
    ) {
        log.info("Get bookings for userId={}", userId);
        return BookingMapper.mapToDto(bookingService.getUserBookings(userId, state, from, size));
    }


}
