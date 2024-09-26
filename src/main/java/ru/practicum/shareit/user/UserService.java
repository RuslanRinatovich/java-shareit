package ru.practicum.shareit.user;

import java.util.Collection;
import java.util.Optional;

public interface UserService {

    Collection<User> getUsers();

    Optional<User> getUser(Long userId);

    Optional<User> getUserByEmail(String email);

    Optional<User> createUser(User user);

    void delete(Long userId);

    Optional<User> updateUser(User user, Long userId);

}
