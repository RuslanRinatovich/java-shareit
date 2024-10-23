package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.item.comment.dto.CommentDto;

import java.util.Set;

@Data
public class ItemsDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long ownerId;
    private ItemBookingDto lastBooking;
    private ItemBookingDto nextBooking;
    private Set<CommentDto> comments;
}