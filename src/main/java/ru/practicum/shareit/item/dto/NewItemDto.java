package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.util.Marker;

@Data
public class NewItemDto {
    @NotBlank(groups = Marker.OnCreate.class, message = "Name is mandatory")
    private String name;
    @NotBlank(groups = Marker.OnCreate.class, message = "Description is mandatory")
    private String description;
    @NotNull(groups = Marker.OnCreate.class, message = "available is mandatory")
    private Boolean available;

}
