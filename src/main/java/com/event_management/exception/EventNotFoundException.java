package com.event_management.exception;

import java.util.UUID;

public class EventNotFoundException extends RuntimeException {

	public EventNotFoundException(UUID id) {
		super("Could not find event " + id);
	}
}
