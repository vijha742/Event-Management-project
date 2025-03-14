package com.event_management.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import com.event_management.model.Role;

public class EventRegistrationSummaryDTO {
    private UUID id;
    private EventBaseDTO event;
    private String status;
    private Role role;
    private LocalDateTime registeredAt; 

    public EventRegistrationSummaryDTO() {}

    public EventRegistrationSummaryDTO(EventRegistration reg) {
        this.id = reg.getId();
        this.status = reg.getStatus();
        this.role = reg.getRole();
        this.registeredAt = reg.getRegisteredAt();
        this.event = new EventBaseDTO(reg.getEvent());
    }
}
