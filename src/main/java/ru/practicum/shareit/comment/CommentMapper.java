package ru.practicum.shareit.comment;

import ru.practicum.shareit.comment.dto.NewCommentDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;

public class CommentMapper {
    public static ItemDto mapToDto(Item item) {
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        dto.setOwnerId(item.getOwner().getId());
        return dto;
    }


    public static Comment newCommentDtoToComment(NewCommentDto newCommentDto, Long userId, Long itemId) {
        Comment comment = new Comment();
        comment.setText(newCommentDto.getText());
        comment.setDescription(newItemDto.getDescription());
        item.setAvailable(newItemDto.getAvailable());
        item.setOwner(null);
        item.setRequest(null);
        return item;
    }
}
