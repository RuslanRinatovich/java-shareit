package ru.practicum.shareit.booking.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
public class BookingItemDto {

    private Long id;
    private String name;
}
