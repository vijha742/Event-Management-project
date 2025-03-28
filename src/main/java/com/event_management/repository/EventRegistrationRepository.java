package com.event_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

import com.event_management.model.Event;
import com.event_management.model.EventRegistration;

public interface EventRegistrationRepository extends JpaRepository<EventRegistration, UUID> {
    List<EventRegistration> findByUserId(UUID userId);
    List<EventRegistration> findByEventId(UUID eventId);

    @Modifying
    @Query("DELETE FROM EventRegistration er WHERE er.event = :event")
    void deleteEvent(@Param("event") Event event);

    @Query("SELECT er FROM EventRegistration er JOIN FETCH er.event WHERE er.user.id = :userId")
    List<EventRegistration> findByUserIdWithEvent(@Param("userId") UUID userId);

    @Query("SELECT er FROM EventRegistration er JOIN FETCH er.user WHERE er.event.id = :eventId")
    List<EventRegistration> findByEventIdWithUser(@Param("userId") UUID eventId);
}

