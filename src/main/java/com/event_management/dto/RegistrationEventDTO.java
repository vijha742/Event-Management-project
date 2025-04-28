package com.event_management.dto;

import com.event_management.model.EventRegistration;
import com.event_management.model.RegistrationStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

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
