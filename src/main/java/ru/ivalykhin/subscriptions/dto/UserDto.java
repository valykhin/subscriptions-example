package ru.ivalykhin.subscriptions.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private UUID id;
    @NotNull
    private String name;
    @NotNull
    private String surname;
    @NotNull
    private String phone;
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
