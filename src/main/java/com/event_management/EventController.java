package com.event_management;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
@Validated
public class EventController {
	private final EventService eventService;

	@GetMapping
	public ResponseEntity<List<Event>> getAllEvents(){
        return ResponseEntity.ok().body(eventService.getAllEvents());
    }
}
