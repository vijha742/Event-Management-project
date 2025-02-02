package com.event_management;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventService {
	private final EventRepository eventRepo;

	public List<Event> getAllEvents() {
		return eventRepo.findAll();
	}

	public Event getEvent(Long Id) {
		return eventRepo.findById(Id)
			.orElseThrow(() -> new EventNotFoundException(Id));
	}

	public Event createEvent(Event event) {
		return eventRepo.save(event);
	}

	public Event updateEvent(Long Id, Event event) {
		Event existingEvent = eventRepo.findById(Id)
			.orElseGet(() -> eventRepo.save(event));
		existingEvent.setName(event.getName());
		existingEvent.setDescription(event.getDescription());
		existingEvent.setLocation(event.getLocation());
		existingEvent.setDate(event.getDate());
		existingEvent.setTime(event.getTime());
		existingEvent.setBanner(event.getBanner());
		existingEvent.setParticipants(event.getParticipants());
		return eventRepo.save(existingEvent);
	}

	public void deleteEvent(Long Id) {
		eventRepo.deleteById(Id);
	}
}

