package ru.ivalykhin.subscriptions.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "subscriptions")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Version
    private Long version;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
