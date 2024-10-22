package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingBookerDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;

public final class BookingMapper {
    public static BookingDto mapToDto(Booking booking) {
        BookingDto dto = new BookingDto();
        dto.setId(booking.getId());
        dto.setEnd(booking.getEnd());
        dto.setStart(booking.getStart());
        BookingItemDto bookingItemDto = new BookingItemDto();
        bookingItemDto.setId(booking.getItem().getId());
        bookingItemDto.setName(booking.getItem().getName());
        dto.setItem(bookingItemDto);
        BookingBookerDto bookingBookerDto = new BookingBookerDto();
        bookingBookerDto.setId(booking.getBooker().getId());
        dto.setBooker(bookingBookerDto);
        dto.setStatus(booking.getStatus().name());
        return dto;
    }


    public static Booking newBookingDtoToBooking(NewBookingDto newBookingDto, Long userId) {
        Booking booking = new Booking();
        booking.setStart(newBookingDto.getStart());
        booking.setEnd(newBookingDto.getEnd());
        booking.setStatus(Status.WAITING);
        booking.getItem().setId(newBookingDto.getItemId());
        booking.getBooker().setId(userId);
        return booking;
    }


}
