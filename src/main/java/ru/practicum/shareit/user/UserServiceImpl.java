package ru.practicum.shareit.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Getter
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserStorage userStorage;

    @Override
    public Collection<User> getUsers() {
        return userStorage.findAll();
    }


    @Override
    public Optional<User> getUser(final long userId) {
        return userStorage.findById(userId);
    }


    @Override
    public Optional<User> getUserByEmail(final String email) {

        return userStorage.findByEmail(email);
    }
    @Override
    public User createUser(final User user) {
        Objects.requireNonNull(user, "Cannot create user: is null");
        Optional<User> user1 = getUserByEmail(user.getEmail());
        if (user1.isPresent()) {
            throw new ValidationException("Пользователь с email = " + user.getEmail() + " уже существует");
        }
        final User userStored = userStorage.save(user);
        log.info("Created new user: {}", userStored);
        return userStored;
    }

    @Override
    public Optional<User> updateUser(final User user) {
        Objects.requireNonNull(user, "Cannot update user: is null");
        final Optional<User> userStored = userStorage.update(user);
        userStored.ifPresent(u -> log.info("Updated user: {}", u));
        return userStored;
    }

    @Override
    public void delete(Long userId) {
        Optional<User> user = userStorage.findById(userId);
        if (user.isEmpty()) {
            log.warn("Пользователь с id = " + userId + " не найден");
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        userStorage.delete(userId);
    }
}
