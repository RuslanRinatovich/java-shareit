package ru.practicum.shareit.user;

import java.util.Collection;
import java.util.Optional;

public interface UserService {

    Collection<User> getUsers();

    Optional<User> getUser(String id);
    Optional<User> getUserByEmail(String email);

    Optional<User>  createUser(User user);
    void delete(String userId);
    Optional<User>  updateUser(User user, String Id);

}
