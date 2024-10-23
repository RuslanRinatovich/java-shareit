package ru.practicum.shareit.item.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewCommentDto {

    @NotBlank
    @Size(max = 2000)
    private String text;
}
