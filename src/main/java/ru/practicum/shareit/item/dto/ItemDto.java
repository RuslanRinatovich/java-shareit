package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

@Data
public class ItemDto {

    private String name;
    private String description;
    private Boolean available;
    private Long ownerId;
    private Long requestId;

}
