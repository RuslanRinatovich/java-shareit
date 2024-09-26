package ru.practicum.shareit.user.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.util.Marker;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUserDto {
    @NotBlank(groups = Marker.OnCreate.class, message = "Name is mandatory")
    private String name;
    @NotBlank(groups = Marker.OnCreate.class, message = "Email is mandatory")
    @Email(groups = {Marker.OnCreate.class, Marker.OnUpdate.class}, message = "Email is wrong")
    private String email;
}
