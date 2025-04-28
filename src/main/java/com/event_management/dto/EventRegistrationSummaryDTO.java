package com.event_management.dto;

import com.event_management.model.EventRegistration;
import com.event_management.model.RegistrationStatus;
import com.event_management.model.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EventRegistrationSummaryDTO {
    private UUID id;
    private EventBaseDTO event;
    private RegistrationStatus status;
    private Role role;
    private LocalDateTime registeredAt;

    public EventRegistrationSummaryDTO(EventRegistration reg) {
        this.id = reg.getId();
        this.status = reg.getStatus();
        this.role = reg.getRole();
        this.registeredAt = reg.getRegisteredAt();
        this.event = new EventBaseDTO(reg.getEvent());
    }
}
