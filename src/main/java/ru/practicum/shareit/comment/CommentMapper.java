package ru.practicum.shareit.comment;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.NewCommentDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;

public class CommentMapper {
    public static CommentDto mapToDto(Comment item) {
        CommentDto dto = new CommentDto();
        dto.setId(item.getId());
        dto.setText(item.getText());
        dto.setAuthorName(item.getAuthor().getName());
        dto.setCreated(item.getCreated());
        return dto;
    }

}
