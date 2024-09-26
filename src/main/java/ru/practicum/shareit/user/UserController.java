package ru.practicum.shareit.user;


import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.util.Marker;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@Validated
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
        log.info("Get users ");
        return UserMapper.mapToDto(userService.getUsers());
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable final Long userId) {
        log.info("Get user userId={}", userId);
        return UserMapper.mapToDto(userService.getUser(userId).get());
    }

    @PostMapping
    @Validated({Marker.OnCreate.class})
    public UserDto createUser(@Valid @RequestBody final NewUserDto newUserDto) {
        log.info("Received POST at /users");
        final User user = UserMapper.mapToUser(newUserDto);
        final UserDto userDto = UserMapper.mapToDto(userService.createUser(user).get());
        log.info("Responded to POST /users: {}", userDto);
        return userDto;
    }

    @PatchMapping("/{userId}")
    @Validated({Marker.OnUpdate.class})
    public UserDto updateUser(@Valid @RequestBody final NewUserDto updateUserDto, @PathVariable final Long userId) {
        log.info("Received PATCH at /users");
        User user = UserMapper.mapToUser(updateUserDto);
        final UserDto userDto = UserMapper.mapToDto(userService.updateUser(user, userId).get());
        log.info("Responded to PATCH at /users: {}", userDto);
        return userDto;
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable(name = "id") final Long userId) {
        log.info("Received DELETE user userId={}", userId);
        userService.delete(userId);
        log.info("Responded to DELETE user userId={}", userId);
    }

}
