package ru.practicum.shareit.item;


import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class ItemInMemoryStorage implements ItemStorage {

    private final Map<Long, Item> items;
    private long lastUsedId;

    public ItemInMemoryStorage() {
        this.items = new HashMap<>();
        this.lastUsedId = 0L;
    }

    public Map<Long, Item> getItems() {
        return items;
    }

    public long getLastUsedId() {
        return lastUsedId;
    }

    @Override
    public Collection<Item> findAll(Long userId) {

        return items.values().stream().filter(item -> item.getOwner().getId() == (long) userId).toList();
    }


    @Override
    public Optional<Item> findById(long id) {

        if (items.containsKey(id))
            return Optional.of(items.get(id));
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
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public void delete(long id) {

    }

    @Override
    public void deleteAll() {

    }
}
