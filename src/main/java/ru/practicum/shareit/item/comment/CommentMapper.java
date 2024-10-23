package ru.practicum.shareit.item.comment;

import ru.practicum.shareit.item.comment.dto.CommentDto;

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
