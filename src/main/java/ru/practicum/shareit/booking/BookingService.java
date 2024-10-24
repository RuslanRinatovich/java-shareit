package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.NewBookingDto;

import java.util.List;

public interface BookingService {


    Booking getBooking(final Long id, final Long userId);

    Booking create(NewBookingDto booking, Long userId);

    Booking changeBookingStatus(long id, boolean isApproved, long userId);

    List<Booking> getBookerBookings(long userId, String status, int from, int size);

    List<Booking> getOwnerBookings(long userId, String status, int from, int size);

    List<Booking> findCompletedBookingForUserAndItem(long userId, long itemId);
}
