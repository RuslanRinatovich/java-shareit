package ru.practicum.shareit.user;


import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.IncorrectParameterException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<UserDto> getUsers() {
        log.info("Received GET at /users");
        Collection<UserDto> dtos = UserMapper.mapToDto(userService.getUsers());
        log.info("Responded to GET /users: {}", dtos);
        return dtos;
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable final String userId) {
        log.info("Received GET at /users/{}", userId);
        final UserDto dto = userService.getUserByEmail(userId).map(UserMapper::mapToDto).orElseThrow(
                () -> new NotFoundException("Пользователь с emailom = " + userId + " не найден")
        );
        log.info("Responded to GET /users/{}: {}", userId, dto);
        return dto;
    }

    @PostMapping
    public UserDto createUser(@Valid @RequestBody final UserDto userDto) {
        log.info("Received POST at /users");
        if (userDto.getEmail() == null) throw new IncorrectParameterException("Email должен быть указан");
        final User user = UserMapper.mapToUser(userDto);
        final UserDto newUserDto = UserMapper.mapToDto(userService.createUser(user));
        log.info("Responded to POST /users: {}", newUserDto);
        return newUserDto;
    }

    @PatchMapping("/{userId}")
    public User updateUser(@Valid @RequestBody final UserUpdateDto updateUserDto,
                           @PathVariable final String userId) {
        log.info("Received PUT at /users");

       return new User();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable(name = "id") final Long userId) {
        if (userId == null) throw new IncorrectParameterException("Id должен быть указан");
        userService.delete(userId);
    }

}
