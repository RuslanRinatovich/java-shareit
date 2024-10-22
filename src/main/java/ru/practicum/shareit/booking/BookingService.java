package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.NewItemDto;

import java.util.Collection;
import java.util.Optional;

public interface BookingService {
 //   Booking getBooking(Long id, Long userId);
    Booking create(NewBookingDto booking, Long userId);

//    Collection<Booking> getUserBookings(long userId, BookingStatusFilter filter, int from, int size);
//
//    Collection<Booking> getOwnerBookings(long userId, BookingStatusFilter filter, int from, int size);
//
//    Collection<Booking> findAllCompleteBookingByUserIdAndItemId(long userId, long itemId);
//
//    Booking applyBooking(long id, boolean isApproved, long userId);

}
