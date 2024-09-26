package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.IncorrectParameterException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/items")
@Slf4j
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/search")
    public Collection<ItemDto> search(@RequestParam(name = "text", required = false) String text) {

        log.info("Received GET at /search with parametr text = {}", text);
        final Collection<ItemDto> dtos = itemService.search(text.toLowerCase()).stream().map(ItemMapper::mapToDto).toList();
        log.info("Responded GET at /search with parametr text = {}", text);
        return dtos;
    }

    @GetMapping("/{id}")
    public ItemDto get(@PathVariable(name = "id") final Long itemId) {
        log.info("Received GET at /items/{}", itemId);
        if (itemId == null) throw new IncorrectParameterException("Id должен быть указан");
        log.info("Responded GET at /items/{}", itemId);
        return ItemMapper.mapToDto(itemService.getItem(itemId).get());
    }

    @GetMapping
    public Collection<ItemDto> getItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Received GET at /items for user{}", userId);
        final Collection<ItemDto> dtos = itemService.getItems(userId).stream().map(ItemMapper::mapToDto).toList();
        log.info("Responded GET at /items for user{}", userId);
        return dtos;
    }

    @PostMapping
    public ItemDto add(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody final NewItemDto newItemDto) {
        log.info("Received POST at /items");
        final Item item = ItemMapper.newItemDtoToItem(newItemDto);

        final ItemDto itemDto = ItemMapper.mapToDto(itemService.create(item, userId).get());
        log.info("Responded to POST /items: {}", itemDto);
        return itemDto;
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody final UpdateItemDto updateItemDto, @PathVariable final Long itemId) {
        log.info("Received PATCH at /items");
        final ItemDto itemDto = ItemMapper.mapToDto(itemService.update(updateItemDto, itemId, userId).get());
        log.info("Responded to PATCH at /items: {}", itemDto);
        return itemDto;
    }


}
