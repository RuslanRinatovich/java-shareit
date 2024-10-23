package ru.practicum.shareit.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.comment.dto.NewCommentDto;

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
        final List<Booking> bookings = bookingService.findAllCompleteBookingByUserIdAndItemId(
                comment.getAuthor().getId(), comment.getItem().getId());
        if (bookings.isEmpty()) {
            throw new ValidationException("item.id", "no complete bookings of item by user");
        }
        final Booking booking = bookings.getFirst();
        comment.getAuthor().setName(booking.getBooker().getName());
        final Comment createdComment = repository.save(comment);
        log.info("Created comment with id = {}: {}", createdComment.getId(), createdComment);
        return createdComment;
    }
}
