package com.event_management;

import java.util.UUID;

class EventNotFoundException extends RuntimeException {

	EventNotFoundException(UUID id) {
		super("Could not find event " + id);
	}
}
