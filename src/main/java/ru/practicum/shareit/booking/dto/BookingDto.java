package ru.practicum.shareit.booking.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@EqualsAndHashCode(of = "id")
public class  BookingDto {

    private Long id;
    private BookingItemDto item;
    private BookingBookerDto booker;
    private LocalDateTime start;
    private LocalDateTime end;
    private String status;
}
