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
    public Optional<Item> create(Item item, String userId) {
        Objects.requireNonNull(item, "Cannot create item: is null");
        if (!isNumeric(userId))
        {
            throw new ValidationException("UserId не корректный");
        }
        long id = Long.parseLong(userId);
        if (userStorage.getUsers().containsKey(id)) {
            User user = userStorage.findById(id).get();
            item.setOwner(user);
            final Item itemStored = itemStorage.save(item);
            log.info("Created new item: {}", itemStored);
            return Optional.of(itemStored);
        }
        throw new NotFoundException("Пользователь с Id = " + id + " не существует");
       }

    @Override
    public void delete(Long itemId) {

    }

    @Override
    public Optional<Item> update(UpdateItemDto item, String itemId, String userId) {
        Objects.requireNonNull(item, "Cannot create item: is null");
        if (!isNumeric(itemId) || !isNumeric(userId)  )
        {
            throw new IncorrectParameterException("Данные не корректны");
        }
        Optional<Item> currentItem =  itemStorage.findById(Long.parseLong(itemId));
        if (currentItem.isEmpty())
        {
            throw new NotFoundException("Item c id "+ itemId + "не найден");
        }
        if (currentItem.get().getOwner().getId() != Long.parseLong(userId))
        {
            throw new NotFoundException("Пользователь c id "+ userId + "не является владельцем Item с id " + itemId);
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

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            long d = Long.parseLong(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
