package com.event_management.dto;

import com.event_management.model.EventRegistration;
import java.util.UUID;

import java.time.LocalDateTime;
import com.event_management.model.RegistrationStatus;
import com.event_management.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegistrationEventDTO {
    private UUID id;
    private UserResponseDTO user;
    private LocalDateTime registeredAt;
    private RegistrationStatus status;

    public RegistrationEventDTO(EventRegistration registration) {
        this.id = registration.getId();
        this.user = new UserResponseDTO(registration.getUser());
        this.registeredAt = registration.getRegisteredAt();
        this.status = registration.getStatus();
    }
}
