package com.event_management;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface EventRegistrationRepository extends JpaRepository<EventRegistration, UUID> {
    List<EventRegistration> findByUserId(UUID userId);
    List<EventRegistration> findByEventId(UUID eventId);
}

