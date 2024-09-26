package ru.practicum.shareit.user;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.IncorrectParameterException;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Getter
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public Collection<User> getUsers() {
        return userStorage.findAll();
    }

    @Override
    public Optional<User> getUser(final Long userId) {
        if (userId == null)
            return Optional.empty();
        return userStorage.findById(userId);
    }

    @Override
    public Optional<User> getUserByEmail(final String email) {
        return userStorage.findByEmail(email);
    }

    @Override
    public User createUser(final User user) {
        Objects.requireNonNull(user, "Cannot create user: is null");
        Optional<User> userWithEmail = getUserByEmail(user.getEmail());
        if (userWithEmail.isPresent()) {
            throw new ConflictException("Пользователь с email = " + user.getEmail() + " уже существует");
        }
        final User userStored = userStorage.save(user);
        log.info("Created new user: {}", userStored);
        return userStored;
    }

    @Override
    public User updateUser(final User user, final Long userId) {
        Objects.requireNonNull(user, "Cannot update user: is null");
        if (userId == null)
            throw new IncorrectParameterException("Данные не корректны");
        user.setId(userId);
        if (userStorage.getUsers().containsKey(userId)) {
            Optional<User> u = getUserByEmail(user.getEmail());
            User currentUser = userStorage.getUsers().get(userId);
            if (u.isPresent() && u.get() != currentUser) {
                throw new ConflictException("Данный email " + user.getEmail() + " уже используется другим пользователем");
            }
            if (user.getName() == null)
                user.setName(currentUser.getName());
            if (user.getEmail() == null)
                user.setEmail(currentUser.getEmail());
            return userStorage.update(user);
        } else {
            throw new NotFoundException("User c id " + userId + "не найден");
        }
    }

    @Override
    public void delete(Long userId) {
        if (userId == null)
            return;
        userStorage.delete(userId);
    }
}
