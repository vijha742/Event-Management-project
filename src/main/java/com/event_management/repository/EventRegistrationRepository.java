package com.event_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

import com.event_management.model.EventRegistration;

public interface EventRegistrationRepository extends JpaRepository<EventRegistration, UUID> {
    List<EventRegistration> findByUserId(UUID userId);
    List<EventRegistration> findByEventId(UUID eventId);
}

