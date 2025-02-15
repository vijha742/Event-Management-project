package com.event_management.controller;

// HACK: To work on this...
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;
import java.util.UUID;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.event_management.dto.EventBaseDTO;
import com.event_management.repository.UserRepository;
import com.event_management.service.EventService;
import com.event_management.model.Event;
import com.event_management.assembler.EventModelAssembler;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/event")
@RequiredArgsConstructor
@Validated
public class EventController {
    private final EventService eventService;
    private final EventModelAssembler assembler;
    private final UserRepository userRepository;

    @GetMapping
    public CollectionModel<EntityModel<Event>> getAllEvents() {
        List<EntityModel<Event>> events =
                eventService.getAllEvents().stream()
                        .map(assembler::toModel)
                        .collect(Collectors.toList());
        return CollectionModel.of(
                events, linkTo(methodOn(EventController.class).getAllEvents()).withSelfRel());
    }

    @GetMapping("/base")
    public CollectionModel<EntityModel<EventBaseDTO>> getAllBaseEvents() {
        List<EntityModel<EventBaseDTO>> eventDTOs =
                eventService.getAllEvents().stream()
                        .map(event -> eventService.convertToBaseDTO(event))
                        .map(assembler::toModel)
                        .collect(Collectors.toList());

        return CollectionModel.of(
                eventDTOs, linkTo(methodOn(EventController.class).getAllEvents()).withSelfRel());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventBaseDTO> getEvent(@PathVariable UUID id) {
        Event event = eventService.getEvent(id);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        EntityModel<Event> entityModel = assembler.toModel(event);
        Event extractedEvent = entityModel.getContent();  // Extract the Event
    
    if (extractedEvent == null) {
        return ResponseEntity.notFound().build();
    }

        EventBaseDTO dto = eventService.convertToBaseDTO(extractedEvent);
        return ResponseEntity.ok(dto);
    }

/*    @PostMapping
    ResponseEntity<?> newEvent(@RequestBody EventDTO newEvent) {
        Optional<User> adminUser = userRepository.findById(newEvent.getAdmin());

        if (adminUser.isEmpty()) {
            return ResponseEntity.badRequest().body("Admin user not found");
        }

        Event createdEvent = eventService.createEvent(newEvent, adminUser.get());
        EntityModel<Event> entityModel = assembler.toModel(createdEvent);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
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
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }
*/
}
