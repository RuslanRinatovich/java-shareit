package ru.practicum.shareit.booking;

import jakarta.persistence.*;
import lombok.Data;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Data

public class Booking {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;

    private Item item;

        private User booker;

    @Enumerated(EnumType.STRING)
    private Status status;

}