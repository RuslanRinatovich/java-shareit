package ru.practicum.shareit.item.comment;

import ru.practicum.shareit.item.comment.dto.NewCommentDto;

public interface CommentService {
    Comment createComment(NewCommentDto newCommentDto, Long userId, Long itemId);
}
