package ru.practicum.shareit.item.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.item.comment.dto.NewCommentDto;
import ru.practicum.shareit.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Validated
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService{

    private final CommentRepository repository;
    private final BookingService bookingService;

    @Override
    @Transactional
    public Comment createComment(NewCommentDto newCommentDto, Long userId, Long itemId) {
        Objects.requireNonNull(newCommentDto, "Cannot create comment: is null");
        final List<Booking> bookings = bookingService.findCompletedBookingForUserAndItem(userId, itemId);
        if (bookings.isEmpty()) {
            throw new ValidationException("no completed bookings of item by user");
        }
        final Booking booking = bookings.getFirst();
        Comment comment = new Comment();
        comment.setCreated(LocalDateTime.now());
        comment.setText(newCommentDto.getText());
        comment.setAuthor(booking.getBooker());
        comment.setItem(booking.getItem());

        final Comment createdComment = repository.save(comment);
        log.info("Created comment with id = {}: {}", createdComment.getId(), createdComment);
        return createdComment;
    }
}
