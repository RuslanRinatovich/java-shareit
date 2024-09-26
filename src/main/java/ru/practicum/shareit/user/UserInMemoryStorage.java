package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserInMemoryStorage implements UserStorage {
    private final Map<Long, User> users;
    private long lastUsedId;

    public UserInMemoryStorage() {
        this.users = new HashMap<>();
        this.lastUsedId = 0L;
    }

    public Map<Long, User> getUsers() {
        return users;
    }

    public long getLastUsedId() {
        return lastUsedId;
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public Optional<User> findById(final long id) {
        if (users.containsKey(id))
            return Optional.of(users.get(id));
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        if (email == null)
            return Optional.empty();
        for (User user : users.values()) {
            if (user.getEmail() == null)
                continue;
            if (user.getEmail().equals(email))
                return Optional.ofNullable(users.get(user.getId()));
        }
        return Optional.empty();
    }

    @Override
    public User save(final User user) {
        user.setId(++lastUsedId);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(final User user) {
        users.put(user.getId(), user);
        return user;
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
