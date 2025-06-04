package ru.ivalykhin.subscriptions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ivalykhin.subscriptions.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByPhone(String phone);
}
