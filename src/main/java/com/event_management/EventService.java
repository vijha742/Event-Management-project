package com.event_management;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventService {
	private final EventRepository eventRepo;

	public List<Event> getAllEvents() {
		return eventRepo.findAll();
	}



}
