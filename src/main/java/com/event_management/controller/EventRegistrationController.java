package com.event_management.controller;

import com.event_management.dto.EventRegistrationSummaryDTO;
import java.util.stream.Collectors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

import com.event_management.service.EventRegistrationService;
import com.event_management.model.EventRegistration;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/registrations")
@RequiredArgsConstructor
@Validated
public class EventRegistrationController {

    private final EventRegistrationService eventRegistrationService;

    @PostMapping("/register")
    public String registerUser(@RequestBody UUID userId, @RequestBody UUID eventId) {
        eventRegistrationService.registerUserForEvent(userId, eventId);
        return "User registered successfully!";
    }

    @GetMapping("/user/{userId}")
    public List<EventRegistrationSummaryDTO> getUserEvents(@PathVariable UUID userId) {
        List<EventRegistration> registrations = eventRegistrationService.getUserEvents(userId);
        return registrations.stream()
            .map(EventRegistrationSummaryDTO::new)
            .collect(Collectors.toList());
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

