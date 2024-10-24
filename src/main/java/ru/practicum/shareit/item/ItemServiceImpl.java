package ru.practicum.shareit.item;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.ItemsDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.*;

@Getter
@Service
@Slf4j
@Validated
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserRepository userRepository;

    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;


    @Override
    public Collection<Item> search(String text) {
        if ((text == null) || text.isBlank() || text.isEmpty())
            return new ArrayList<>();
        return itemRepository.findAll().stream().filter(item ->
                (item.getName().toLowerCase().contains(text) || item.getDescription().toLowerCase().contains(text))
                        && item.getAvailable()).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Item> getItems(Long userId) {
        if (userId == null) {
            return new ArrayList<>();
        }

        if (userRepository.existsById(userId)) {

            return itemRepository.findAllByOwner(userRepository.findById(userId).get());
        }
        throw new NotFoundException("Пользователь с Id = " + userId + " не существует");
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemsDto> getItemsWithBookingAndComments(Long userId) {
        if (userId == null)
            return new ArrayList<>();
        List<ItemsDto> result = new ArrayList<>();
        for (Item i : itemRepository.findAllByOwner(userRepository.findById(userId).get())) {

            Long itemId = i.getId();
            Set<Comment> comments = new HashSet<>(commentRepository.findAllById(Collections.singleton(itemId)));

            Booking last = null;
            Booking next = null;

            if (i.getOwner().getId().equals(userId)) {
                Optional<Booking> lastBooking = bookingRepository.findLastItemBooking(itemId);
                Optional<Booking> nextBooking = bookingRepository.findNextItemBooking(itemId);
                if (lastBooking.isPresent())
                    last = lastBooking.get();

                if (nextBooking.isPresent())
                    next = nextBooking.get();
            }

            result.add(ItemMapper.mapToItemsDto(i, last, next, comments));
        }
        return result;
    }

    @Override
    @Transactional
    public ItemsDto getItemWithBookingAndComments(Long userId, Long itemId) {
        Objects.requireNonNull(userId, "Cannot load booking: userId is null");
        Objects.requireNonNull(itemId, "Cannot load booking: itemId is null");
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item с Id = " + itemId + " не существует"));
        Set<Comment> comments = item.getComments();
        Booking last = null;
        Booking next = null;

        if (item.getOwner().getId().equals(userId)) {
            Optional<Booking> lastBooking = bookingRepository.findLastItemBooking(itemId);
            Optional<Booking> nextBooking = bookingRepository.findNextItemBooking(itemId);
            if (lastBooking.isPresent())
                last = lastBooking.get();

            if (nextBooking.isPresent())
                next = nextBooking.get();
        }

        return ItemMapper.mapToItemsDto(item, last, next, comments);
    }

    @Override
    public boolean existsById(final long id) {
        return itemRepository.existsById(id);
    }

    @Override
    public Optional<Item> getItem(Long itemId) {
        if (itemId == null)
            return Optional.empty();
        return itemRepository.findByIdWithAdditionalData(itemId);
    }

    @Override
    @Transactional
    public Item create(Item item, Long userId) {
        Objects.requireNonNull(item, "Cannot create item: is null");
        Objects.requireNonNull(userId, "Cannot load user: userId is null");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с Id = " + userId + " не существует"));
        item.setOwner(user);
        final Item itemStored = itemRepository.save(item);
        log.info("Created new item: {}", itemStored);
        return itemStored;
    }


    @Override
    @Transactional
    public boolean existByOwnerId(long userId) {
        return itemRepository.existsByOwnerId(userId);
    }

    @Override
    public Item update(NewItemDto item, Long itemId, Long userId) {
        Objects.requireNonNull(item, "Cannot update item: is null");
        Objects.requireNonNull(userId, "Cannot update item: userId is null");
        Objects.requireNonNull(itemId, "Cannot update item: itemId is null");
        Item updateItem = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Item c id " + itemId + "не найден"));
        long ownerId = updateItem.getOwner().getId();
        if (!userId.equals(ownerId)) {
            throw new NotFoundException("Пользователь c id " + userId + "не является владельцем Item с id " + itemId);
        }
        if (item.getName() != null)
            updateItem.setName(item.getName());
        if (item.getDescription() != null)
            updateItem.setDescription(item.getDescription());
        if (item.getAvailable() != null)
            updateItem.setAvailable(item.getAvailable());
        final Item itemUpdated = itemRepository.save(updateItem);
        log.info("Updated item: {}", itemUpdated);
        return itemUpdated;
    }
}
