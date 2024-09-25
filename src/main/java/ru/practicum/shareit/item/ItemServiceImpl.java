package ru.practicum.shareit.item;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.IncorrectParameterException;
import ru.practicum.shareit.exception.InternalServerException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.UserStorage;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Getter
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private final UserStorage userStorage;
    @Autowired
    private final ItemStorage itemStorage;


    @Override
    public Collection<Item> getItems() {
        return null;
    }

    @Override
    public Optional<Item> getItem(Long itemId) {
        return Optional.empty();
    }

    @Override
    public Optional<Item> create(Item item, Long userId) {
        Objects.requireNonNull(item, "Cannot create item: is null");

        //userStorage.findById(userId);
        if (userStorage.getUsers().containsKey(userId)) {
            User user = userStorage.findById(userId).get();
            item.setOwner(user);
            final Item itemStored = itemStorage.save(item);
            log.info("Created new item: {}", itemStored);
            return Optional.of(itemStored);
        }
        throw new IncorrectParameterException("Пользователь с Id = " + userId + " не существует");
       }

    @Override
    public void delete(Long itemId) {

    }

    @Override
    public Optional<Item> update(UpdateItemDto item, Long itemId) {
        return Optional.empty();
    }
}
