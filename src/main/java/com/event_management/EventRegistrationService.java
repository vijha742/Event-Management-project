package com.event_management;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventRegistrationService {

    private EventRegistrationRepository eventRegistrationRepository;
    private UserRepository userRepository;
    private EventRepository eventRepository;

    public void registerUserForEvent(UUID userId, UUID eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found"));

        EventRegistration registration = new EventRegistration();
        registration.setUser(user);
        registration.setEvent(event);

        eventRegistrationRepository.save(registration);
    }

    public List<EventRegistration> getUserEvents(UUID userId) {
        return eventRegistrationRepository.findByUserId(userId);
    }

    public List<EventRegistration> getEventParticipants(UUID eventId) {
        return eventRegistrationRepository.findByEventId(eventId);
    }

    public void markEventAsAttended(UUID userId, UUID eventId) {
        EventRegistration registration = eventRegistrationRepository.findByUserId(userId)
            .stream()
            .filter(reg -> reg.getEvent().getId().equals(eventId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("User is not registered for this event"));

        registration.setStatus(RegistrationStatus.ATTENDED);
        eventRegistrationRepository.save(registration);
    }
}

