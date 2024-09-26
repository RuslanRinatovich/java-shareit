package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.UpdateItemDto;

import java.util.Collection;
import java.util.Optional;

public interface ItemService {

    Collection<Item> getItems(Long userId);

    Collection<Item> search(String text);

    Optional<Item> getItem(Long itemId);

    Optional<Item> create(Item item, Long userId);

    void delete(Long itemId);

    Optional<Item> update(UpdateItemDto item, Long itemId, Long userId);

}

