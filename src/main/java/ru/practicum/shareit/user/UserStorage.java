package ru.practicum.shareit.user;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface UserStorage {
    Collection<User> findAll();

    Map<Long, User> getUsers();

    Optional<User> findById(long id);

    Optional<User> findByEmail(String email);

    User save(User user);

    User update(User user);

    void delete(long id);

    void deleteAll();
}
