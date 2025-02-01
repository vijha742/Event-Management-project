package com.event_management;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.event_management.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
}
