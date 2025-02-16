package com.event_management.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.event_management.model.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
		@Query("SELECT e FROM Event e LEFT JOIN FETCH e.admin LEFT JOIN FETCH e.eventRegistrations er LEFT JOIN FETCH er.user")
		List<Event> findAllWithAdminAndRegistrations();
}
