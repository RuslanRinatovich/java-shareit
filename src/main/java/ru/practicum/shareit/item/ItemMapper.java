package ru.practicum.shareit.item;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemsDto;
import ru.practicum.shareit.item.dto.NewItemDto;

import java.util.Set;
import java.util.stream.Collectors;

public final class ItemMapper {
    public static ItemDto mapToDto(Item item) {
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        dto.setOwnerId(item.getOwner().getId());
        if (item.getComments() != null)
            dto.setComments(item.getComments().stream().map(CommentMapper::mapToDto).collect(Collectors.toSet()));
        else
            dto.setComments(null);
        return dto;
    }

    public static ItemsDto mapToItemsDto(Item item, Booking last, Booking next, Set<Comment> commentSet) {
        ItemsDto dto = new ItemsDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        dto.setOwnerId(item.getOwner().getId());
        dto.setComments(commentSet.stream().map(CommentMapper::mapToDto).collect(Collectors.toSet()));
        dto.setLastBooking(mapBookingToItemBookingDto(last));
        dto.setNextBooking(mapBookingToItemBookingDto(next));
        return dto;
    }

    public static ItemBookingDto mapBookingToItemBookingDto(Booking booking) {
        ItemBookingDto itemBookingDto = new ItemBookingDto();
        if (booking != null) {
            itemBookingDto.setBookerId(booking.getBooker().getId());
            itemBookingDto.setId(booking.getId());
        }
        return itemBookingDto;
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
