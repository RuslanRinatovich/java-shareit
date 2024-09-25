package ru.practicum.shareit.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.ValidationException;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Getter
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private final UserStorage userStorage;

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            long d = Long.parseLong(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @Override
    public Collection<User> getUsers() {
        return userStorage.findAll();
    }

    @Override
    public Optional<User> getUser(final String userId) {
        if (!isNumeric(userId))
            return Optional.of(new User());
        return userStorage.findById(Long.parseLong(userId));
    }

    @Override
    public Optional<User> getUserByEmail(final String email) {
        return userStorage.findByEmail(email);
    }

    @Override
    public Optional<User> createUser(final User user) {
        Objects.requireNonNull(user, "Cannot create user: is null");
        Optional<User> userWithEmail = getUserByEmail(user.getEmail());
        if (userWithEmail.isPresent()) {
            throw new ConflictException("Пользователь с email = " + user.getEmail() + " уже существует");
        }
        final User userStored = userStorage.save(user);
        log.info("Created new user: {}", userStored);
        return Optional.of(userStored);
    }

    @Override
    public Optional<User> updateUser(final User user, final String userId) {
        Objects.requireNonNull(user, "Cannot update user: is null");
        if (!isNumeric(userId)) {
            throw new ValidationException("UserId не корректный");
        }
        user.setId(Long.parseLong(userId));
        if (userStorage.getUsers().containsKey(user.getId())) {
            Optional<User> u = getUserByEmail(user.getEmail());
            User currentUser = userStorage.getUsers().get(user.getId());
            if (u.isPresent() && u.get() != currentUser) {
                return Optional.empty();
            }
            if (user.getName() == null)
                user.setName(currentUser.getName());
            if (user.getEmail() == null)
                user.setEmail(currentUser.getEmail());
            return Optional.of(userStorage.update(user));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void delete(String userId) {
        if (isNumeric(userId))
            userStorage.delete(Long.parseLong(userId));
    }
}
