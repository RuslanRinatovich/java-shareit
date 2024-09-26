package ru.practicum.shareit.item;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.IncorrectParameterException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Getter
@Service
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final UserStorage userStorage;

    private final ItemStorage itemStorage;

    public ItemServiceImpl(UserStorage userStorage, ItemStorage itemStorage) {
        this.userStorage = userStorage;
        this.itemStorage = itemStorage;
    }



    @Override
    public Collection<Item> search(String text) {
        if ((text == null) || text.isBlank() || text.isEmpty())
            return new ArrayList<>();
        return itemStorage.getItems().values().stream().filter(item ->
                (item.getName().toLowerCase().contains(text) || item.getDescription().toLowerCase().contains(text))
                        && item.getAvailable()).toList();
    }

    @Override
    public Collection<Item> getItems(Long userId) {
        if (userId == null) {
           return new ArrayList<>();
        }

        if (userStorage.getUsers().containsKey(userId)) {

            return itemStorage.findAll(userId);
        }
        throw new NotFoundException("Пользователь с Id = " + userId + " не существует");
    }

    @Override
    public Optional<Item> getItem(Long itemId) {
        if (itemId == null)
            return Optional.empty();
        return itemStorage.findById(itemId);
    }

    @Override
    public Optional<Item> create(Item item, Long userId) {
        Objects.requireNonNull(item, "Cannot create item: is null");
        if (userId == null) {
            throw new ValidationException("UserId не корректный");
        }

        if (userStorage.getUsers().containsKey(userId)) {
            User user = userStorage.findById(userId).get();
            item.setOwner(user);
            final Item itemStored = itemStorage.save(item);
            log.info("Created new item: {}", itemStored);
            return Optional.of(itemStored);
        }
        throw new NotFoundException("Пользователь с Id = " + userId + " не существует");
    }

    @Override
    public void delete(Long itemId) {

    }

    @Override
    public Optional<Item> update(NewItemDto item, Long itemId, Long userId) {
        Objects.requireNonNull(item, "Cannot create item: is null");
        if (itemId == null || userId == null) {
            throw new IncorrectParameterException("Данные не корректны");
        }
        Optional<Item> currentItem = itemStorage.findById(itemId);
        if (currentItem.isEmpty()) {
            throw new NotFoundException("Item c id " + itemId + "не найден");
        }
        long ownerId = currentItem.get().getOwner().getId();

        if (!userId.equals(ownerId)) {
            throw new NotFoundException("Пользователь c id " + userId + "не является владельцем Item с id " + itemId);
        }
        Item updateItem = currentItem.get();
        if (item.getName() != null)
            updateItem.setName(item.getName());
        if (item.getDescription() != null)
            updateItem.setDescription(item.getDescription());
        if (item.getAvailable() != null)
            updateItem.setAvailable(item.getAvailable());
        final Item itemUpdated = itemStorage.update(updateItem);
        log.info("Updated item: {}", itemUpdated);
        return Optional.of(itemUpdated);
    }
}
