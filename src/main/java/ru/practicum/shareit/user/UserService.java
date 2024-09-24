package ru.practicum.shareit.user;

import java.util.Collection;
import java.util.Optional;

public interface UserService {

    Collection<User> getUsers();

    Optional<User> getUser(long id);
    Optional<User> getUserByEmail(String email);

    User createUser(User user);
    void delete(Long userId);
    Optional<User> updateUser(User user);

}
