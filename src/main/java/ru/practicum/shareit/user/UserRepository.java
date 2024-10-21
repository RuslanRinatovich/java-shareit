package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

interface UserRepository extends JpaRepository<User, Long> {
    User findOneByEmail(String email);
}
