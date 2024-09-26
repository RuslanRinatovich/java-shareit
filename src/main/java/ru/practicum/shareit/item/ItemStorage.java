package ru.practicum.shareit.item;


import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface ItemStorage {

    Collection<Item> findAll(Long userId);

    Map<Long, Item> getItems();

    Optional<Item> findById(long id);

    Item save(Item item);

    Item update(Item item);

    void delete(long id);

    void deleteAll();

}
