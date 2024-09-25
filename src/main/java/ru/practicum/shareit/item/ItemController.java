package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/items")
public class ItemController {
    private static final Logger log = LoggerFactory.getLogger(ItemController.class);
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }



    @PostMapping
    public ItemDto add(@RequestHeader("X-Sharer-User-Id") String userId, @Valid @RequestBody final NewItemDto newItemDto) {
        log.info("Received POST at /items");
        final Item item = ItemMapper.newItemDtoToItem(newItemDto);

        final ItemDto itemDto = ItemMapper.mapToDto(itemService.create(item, userId).get());
        log.info("Responded to POST /items: {}", itemDto);
        return itemDto;
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") String userId, @Valid @RequestBody final UpdateItemDto updateItemDto, @PathVariable final String itemId) {
        log.info("Received PATCH at /items");
        final ItemDto itemDto = ItemMapper.mapToDto(itemService.update(updateItemDto, itemId, userId).get());
        log.info("Responded to PATCH at /items: {}", itemDto);
        return itemDto;
    }

}
