package ru.practicum.shareit.comment.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of = "id")
public class CommentDto {

    private Long id;
    private String authorName;
    private String text;
    private LocalDateTime created;
}
