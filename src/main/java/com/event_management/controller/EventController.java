package com.event_management.controller;

// HACK: To work on this...
import com.event_management.model.User;
import java.util.Optional;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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

import com.event_management.assembler.EventModelAssembler;
import com.event_management.dto.EventBaseDTO;
import com.event_management.dto.EventDTO;
import com.event_management.dto.EventResponseDTO;
import com.event_management.model.Event;
import com.event_management.model.Role;
import com.event_management.repository.UserRepository;
import com.event_management.service.EventService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
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
        List<Event> base =  eventService.getAllEvents();
        List<EntityModel<Event>> events = base.stream()
                        .map(assembler::toModel)
                        .collect(Collectors.toList());
        return CollectionModel.of(
                events, linkTo(methodOn(EventController.class).getAllEvents()).withSelfRel());
    }

    @GetMapping("/event-list")
    public CollectionModel<EntityModel<EventBaseDTO>> getAllBaseEvents() {
        List<EventBaseDTO> base = eventService.getAllEventsBaseDTOs();
        List<EntityModel<EventBaseDTO>> eventDTOs = base.stream()
                        .map(assembler::toModel)
                        .collect(Collectors.toList());

        return CollectionModel.of(
                eventDTOs, linkTo(methodOn(EventController.class).getAllEvents()).withSelfRel());
    }

    @Transactional(readOnly = true)
    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getEvent(@PathVariable UUID id) {
        Event event = eventService.getEvent(id);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok( new EventResponseDTO(event));
    }

    @PostMapping
    ResponseEntity<?> newEvent(@RequestBody EventDTO newEvent) {
        Optional<User> adminUser = userRepository.findById(newEvent.getAdmin());

        if (adminUser.isEmpty() || adminUser.get().getRole() == Role.USER) {
            return ResponseEntity.badRequest().body("No user found with this rights...");
        }

        Event createdEvent = eventService.createEvent(newEvent, adminUser.get());
        EntityModel<Event> entityModel = assembler.toModel(createdEvent);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }


    //TODO:Correct the delete mapping logic to let only the User who created the event and the Admins be able to delete the event...
    @DeleteMapping("/{eventId}")
    @Transactional
    public ResponseEntity<?> deleteEvent(@PathVariable UUID eventId) {
        try {
            Event event = eventService.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));
            
            eventService.deleteEvent(event);
            return ResponseEntity.ok().body("Deleted event successfully.");
                
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e);
        }
    }

    @PutMapping("/{id}")
    ResponseEntity<?> replaceEvent(@RequestBody Event newEvent, @PathVariable UUID id) {
        Event updatedEvent = eventService.updateEvent(id, newEvent);
        EntityModel<Event> entityModel = assembler.toModel(updatedEvent);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

}
