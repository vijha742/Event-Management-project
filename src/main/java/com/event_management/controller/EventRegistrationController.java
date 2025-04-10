package com.event_management.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.event_management.dto.EventRegistrationSummaryDTO;
import com.event_management.dto.UserResponseDTO;
import com.event_management.model.EventRegistration;
import com.event_management.service.EventRegistrationService;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/registrations")
@RequiredArgsConstructor
@Validated
public class EventRegistrationController {

    private final EventRegistrationService eventRegistrationService;

    @PostMapping("/register/{userId}")
    public String registerUser(@PathVariable UUID userId, @RequestBody UUID eventId) {
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
    public List<UserResponseDTO> getEventParticipants(@PathVariable UUID eventId) {
        List<EventRegistration> registrations = eventRegistrationService.getEventParticipants(eventId);
        return registrations.stream()
            .map(registration -> registration.getUser())
            .map(UserResponseDTO::new)
            .collect(Collectors.toList());
    }

    //TODO:Add to Enum Missed also so, if the user's status isn't filled till the completio of the event, he/she is marked as absent...
    @PutMapping("/mark-attended/{userId}/{eventId}")
    public String markAsAttended(@PathVariable UUID userId, @PathVariable UUID eventId) {
        eventRegistrationService.markEventAsAttended(userId, eventId);
        return "Event marked as attended!";
    }
}

