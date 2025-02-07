package com.event_management;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/event-registrations")
@RequiredArgsConstructor
@Validated
public class EventRegistrationController {

    private final EventRegistrationService eventRegistrationService;

    @PostMapping("/register/{userId}/{eventId}")
    public String registerUser(@PathVariable UUID userId, @PathVariable UUID eventId) {
        eventRegistrationService.registerUserForEvent(userId, eventId);
        return "User registered successfully!";
    }

    @GetMapping("/user/{userId}")
    public List<EventRegistration> getUserEvents(@PathVariable UUID userId) {
        return eventRegistrationService.getUserEvents(userId);
    }

    @GetMapping("/event/{eventId}")
    public List<EventRegistration> getEventParticipants(@PathVariable UUID eventId) {
        return eventRegistrationService.getEventParticipants(eventId);
    }

    @PutMapping("/mark-attended/{userId}/{eventId}")
    public String markAsAttended(@PathVariable UUID userId, @PathVariable UUID eventId) {
        eventRegistrationService.markEventAsAttended(userId, eventId);
        return "Event marked as attended!";
    }
}

