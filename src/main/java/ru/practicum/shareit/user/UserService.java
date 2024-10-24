package ru.practicum.shareit.user;

import java.util.Collection;
import java.util.Optional;

public interface UserService {

    Collection<User> getUsers();

    Optional<User> getUser(Long userId);

    Optional<User> getUserByEmail(String email);

    User createUser(User user);

    boolean existsById(long id);

    void delete(Long userId);

    User updateUser(User user, Long userId);

}
