package ru.practicum.shareit.user;


import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class UserMapper {

    public static UserDto mapToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        return dto;
    }

    public static Collection<UserDto> mapToDto(Collection<User> users)
    {
        List<UserDto> dtos = new ArrayList<>();
        for (User user: users)
        {
            dtos.add(mapToDto(user));
        }
        return dtos;
    }

    public static User mapToUser(NewUserDto dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        return user;
    }

    public static User updateUserMapToUser(UpdateUserDto dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        return user;
    }
}
