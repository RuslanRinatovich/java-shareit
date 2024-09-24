package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        ItemDto dto = new ItemDto();
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        dto.setOwnerId(null);

        dto.setRequestId(null);
        if (item.getRequest() != null)
            dto.setRequestId(item.getRequest().getId());
        return dto;
    }

}
