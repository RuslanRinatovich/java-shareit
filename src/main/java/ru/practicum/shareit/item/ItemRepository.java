package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Collection<Item> findAllByOwner(User user);
    boolean existsByOwnerId(long userId);

    Optional<Item> findByIdAndOwnerId(Long itemId, Long userId);

    @Query("select i from Item i left join fetch i.comments c left join fetch c.author where i.id = :id")
    Optional<Item> findByIdWithAdditionalData(@Param("id") long id);


}
