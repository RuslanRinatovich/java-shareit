package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentService;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.NewCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemsDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.util.Marker;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */

@Validated
@RestController
@RequestMapping(path = "/items")
@Slf4j
public class ItemController {
    private final ItemService itemService;

    private final CommentService commentService;
    public ItemController(ItemService itemService, CommentService commentService) {
        this.itemService = itemService;
        this.commentService = commentService;
    }

    @GetMapping("/search")
    public Collection<ItemDto> search(@RequestParam(name = "text", required = false) String text) {
        log.info("Search item by text={}", text);
        return itemService.search(text.toLowerCase()).stream().map(ItemMapper::mapToDto).toList();
    }

    @GetMapping("/{id}")
    public ItemDto get(@PathVariable(name = "id") final Long itemId) {
        log.info("Get item itemId = {}", itemId);
        return ItemMapper.mapToDto(itemService.getItem(itemId).get());
    }

    @GetMapping
    public Collection<ItemDto> getItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get items for user userId = {}", userId);
        return itemService.getItems(userId).stream().map(ItemMapper::mapToDto).toList();
    }

    @PostMapping
    @Validated({Marker.OnCreate.class})
    public ItemDto add(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody final NewItemDto newItemDto) {
        log.info("Received POST at /items");
        return ItemMapper.mapToDto(itemService.create(ItemMapper.newItemDtoToItem(newItemDto), userId));
    }

    @PostMapping("/{id}/comment")
    public CommentDto createComment(
            @RequestHeader("X-Sharer-User-Id") final long userId,
            @PathVariable final long id,
            @RequestBody @Valid final NewCommentDto newCommentDto
    ) {
        log.info("Received POST at comment");
        return CommentMapper.mapToDto(commentService.createComment(newCommentDto, userId, id));
    }

    @PatchMapping("/{itemId}")
    @Validated({Marker.OnUpdate.class})
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody final NewItemDto updateItemDto, @PathVariable final Long itemId) {
        log.info("Received PATCH at /items");
        return  ItemMapper.mapToDto(itemService.update(updateItemDto, itemId, userId));
    }
}
