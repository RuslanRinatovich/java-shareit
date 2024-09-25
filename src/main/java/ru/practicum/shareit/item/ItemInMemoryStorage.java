package ru.practicum.shareit.item;


import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class ItemInMemoryStorage implements ItemStorage {

    private long lastUsedId;
    private final Map<Long, Item> items;

    public Map<Long, Item> getItems() {
        return items;
    }

    public ItemInMemoryStorage() {
        this.items = new HashMap<>();
        this.lastUsedId = 0L;
    }


    public long getLastUsedId() {
        return lastUsedId;
    }

    @Override
    public Collection<Item> findAll() {
        return items.values();
    }


    @Override
    public Optional<Item> findById(long id) {
        return Optional.empty();
    }

    @Override
    public Item save(Item item) {
        item.setId(++lastUsedId);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Item item) {
        return null;
    }

    @Override
    public void delete(long id) {

    }

    @Override
    public void deleteAll() {

    }
}
