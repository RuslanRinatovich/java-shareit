package ru.practicum.shareit.user;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Getter
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    public UserServiceImpl(UserRepository userRepository) {
        this.repository = userRepository;
    }

    @Override
    @Transactional
    public User createUser(final User user) {
        Objects.requireNonNull(user, "Cannot create user: user is null");
        Optional<User> userWithEmail = getUserByEmail(user.getEmail());
        if (userWithEmail.isPresent()) {
            throw new ConflictException("user with email = " + user.getEmail() + " exists");
        }
        final User userStored = repository.save(user);
        log.info("Created new user: {}", userStored);
        return userStored;
    }

    @Override
    public Optional<User> getUser(final Long userId) {
        if (userId == null)
            return Optional.empty();
        return repository.findById(userId);
    }

    @Override
    public Collection<User> getUsers() {
        return repository.findAll();
    }

    @Override
    public boolean existsById(final long id) {
        return repository.existsById(id);
    }

    @Override
    public Optional<User> getUserByEmail(final String email) {
        return Optional.ofNullable(repository.findOneByEmail(email));
    }


    @Override
    @Transactional
    public User updateUser(final User user, final Long userId) {
        Objects.requireNonNull(user, "Cannot update user: is null");
        Objects.requireNonNull(userId, "Cannot update user: userId is null");
        user.setId(userId);
        if (repository.existsById(userId)) {
            Optional<User> u = getUserByEmail(user.getEmail());
            User currentUser = repository.findById(userId).get();
            if (u.isPresent() && u.get() != currentUser) {
                throw new ConflictException("Current email " + user.getEmail() + " in use by another user");
            }
            if (user.getName() == null)
                user.setName(currentUser.getName());
            if (user.getEmail() == null)
                user.setEmail(currentUser.getEmail());
            return repository.save(user);
        } else {
            throw new NotFoundException("User c id " + userId + "не найден");
        }
    }

    @Override
    @Transactional
    public void delete(Long userId) {
        if (userId == null)
            return;
        if (!existsById(userId))
            throw new NotFoundException("User c id " + userId + "не найден");
        repository.delete(getUser(userId).get());
    }
}
