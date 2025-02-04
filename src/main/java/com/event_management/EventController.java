package com.event_management;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.hateoas.IanaLinkRelations;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/event")
@RequiredArgsConstructor
@Validated
public class EventController {
	private final EventService eventService;
	private final EventModelAssembler assembler;

	@GetMapping
	CollectionModel<EntityModel<Event>> getAllEvents() {
	List<EntityModel<Event>> events = eventService.getAllEvents().stream()
		.map(assembler::toModel) 
		.collect(Collectors.toList());
		return CollectionModel.of(events, linkTo(methodOn(EventController.class).getAllEvents()).withSelfRel());
	}

	@PostMapping
	ResponseEntity<?> newEvent(@RequestBody Event newEvent) {
		EntityModel<Event> entityModel = assembler.toModel(eventService.createEvent(newEvent));
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}

	@GetMapping("/{id}")
	public EntityModel<Event> getEvent(@PathVariable UUID id) {
		Event event = eventService.getEvent(id);
		return assembler.toModel(event);
	}

	@DeleteMapping("/{id}")
	ResponseEntity<?> deleteEvent(@PathVariable UUID id) {
		eventService.deleteEvent(id);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{id}")
	ResponseEntity<?> replaceEvent(@RequestBody Event newEvent, @PathVariable UUID id) {
		Event updatedEvent = eventService.updateEvent(id, newEvent);
		EntityModel<Event> entityModel = assembler.toModel(updatedEvent);
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}



}
