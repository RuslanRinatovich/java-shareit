package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;

public final class ItemMapper {
    public static ItemDto mapToDto(Item item) {
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        dto.setOwnerId(item.getOwner().getId());
        return dto;
    }


    public static Item newItemDtoToItem(NewItemDto newItemDto) {
        Item item = new Item();
        item.setName(newItemDto.getName());
        item.setDescription(newItemDto.getDescription());
        item.setAvailable(newItemDto.getAvailable());
        item.setOwner(null);

        item.setRequest(null);
        return item;
    }


}
