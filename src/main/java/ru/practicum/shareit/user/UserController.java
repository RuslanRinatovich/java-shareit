package ru.practicum.shareit.user;


import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

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
        final UserDto user = UserMapper.mapToDto(userService.getUser(userId).get());
        log.info("Responded to GET /users/{}: {}", userId, user);
        return user;
    }

    @PostMapping
    public UserDto createUser(@Valid @RequestBody final NewUserDto newUserDto) {
        log.info("Received POST at /users");
        final User user = UserMapper.mapToUser(newUserDto);
        final UserDto userDto = UserMapper.mapToDto(userService.createUser(user).get());
        log.info("Responded to POST /users: {}", userDto);
        return userDto;
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@Valid @RequestBody final UpdateUserDto updateUserDto, @PathVariable final String userId) {
        log.info("Received PATCH at /users");
        User user = UserMapper.updateUserMapToUser(updateUserDto);
        final UserDto userDto = UserMapper.mapToDto(userService.updateUser(user, userId).get());
        log.info("Responded to PATCH at /users: {}", userDto);
        return userDto;
    }

    @DeleteMapping("/{id}")

    public void deleteUser(@PathVariable(name = "id") final String userId) {

        userService.delete(userId);
    }

}
