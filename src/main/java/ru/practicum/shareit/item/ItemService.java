package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemsDto;
import ru.practicum.shareit.item.dto.NewItemDto;

import java.util.Collection;
import java.util.Optional;

public interface ItemService {

    Collection<Item> getItems(Long userId);

    Collection<ItemsDto> getItemsWithBookingAndComments(Long userId);

    ItemsDto getItemWithBookingAndComments(Long userId, Long itemId);

    Collection<Item> search(String text);

    Optional<Item> getItem(Long itemId);

    Item create(Item item, Long userId);

    boolean existsById(long id);

    boolean existByOwnerId(long userId);

    Item update(NewItemDto item, Long itemId, Long userId);

}

