package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserInMemoryStorage implements UserStorage {
    private long lastUsedId;
    private final Map<Long, User> users;

    public UserInMemoryStorage() {
        this.users = new HashMap<>();
        this.lastUsedId = 0L;
    }
    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public Optional<User> findById(final long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        if (email == null)
            return Optional.empty();
        for (User user: users.values())
        {
            if (user.getEmail() == null)
                continue;
            if (user.getEmail().equals(email))
                return Optional.ofNullable(users.get(user.getId()));
        }
        return Optional.empty();
    }

    @Override
    public User save(final User user) {
        Objects.requireNonNull(user, "Cannot save user: is null");
        user.setId(++lastUsedId);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> update(final User user) {
        Objects.requireNonNull(user, "Cannot update user: is null");
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void delete(final long id) {

        users.remove(id);
    }

    @Override
    public void deleteAll() {

        users.clear();
    }
}
