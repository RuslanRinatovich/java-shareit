package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.user.UserController;

import java.util.Collection;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final BookingService bookingService;

    @PostMapping
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
    public Collection<BookingDto> getBookerBookings(
            @RequestHeader("X-Sharer-User-Id") final long userId,
            @RequestParam(defaultValue = "ALL") final String status,
            @RequestParam(defaultValue = "0") @PositiveOrZero final int from,
            @RequestParam(defaultValue = "10") @Positive final int size
    ) {
        log.info("Get bookings for booker with userId={}", userId);
        return BookingMapper.mapToDto(bookingService.getBookerBookings(userId, status, from, size));
    }

    @GetMapping("/owner")
    public Collection<BookingDto> getOwnerBookings(
            @RequestHeader("X-Sharer-User-Id") final long userId,
            @RequestParam(defaultValue = "ALL") final String status,
            @RequestParam(defaultValue = "0") @PositiveOrZero final int from,
            @RequestParam(defaultValue = "10") @Positive final int size
    ) {
        log.info("Get bookings for owner with userId={}", userId);
        return BookingMapper.mapToDto(bookingService.getOwnerBookings(userId, status, from, size));
    }

    @PatchMapping("/{id}")
    public BookingDto changeBookingStatus(
            @RequestHeader("X-Sharer-User-Id") final long userId,
            @PathVariable final long id,
            @RequestParam final boolean approved
    ) {
        log.info("Patch bookings for owner userId={}", userId);
        return BookingMapper.mapToDto(bookingService.changeBookingStatus(id, approved, userId));
    }

}
