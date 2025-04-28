package com.event_management.service;

import com.event_management.assembler.EventModelAssembler;
import com.event_management.dto.EventBaseDTO;
import com.event_management.dto.EventDTO;
import com.event_management.model.Event;
import com.event_management.model.User;
import com.event_management.repository.EventRegistrationRepository;
import com.event_management.repository.EventRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class EventService {

    private final EventRepository eventRepo;
    private final EventModelAssembler eventAssembler;
    private final EventRegistrationRepository eventRegistrationRepo;

    @Transactional(readOnly = true)
    public List<Event> getAllEvents() {
        return List.copyOf(eventRepo.findAllWithAdminAndRegistrations());
    }

    @Transactional(readOnly = true)
    public List<EventBaseDTO> getAllEventsBaseDTOs() {
        List<Event> event = eventRepo.findAllWithAdminAndRegistrations();
        return event.stream()
                .map(EventBaseDTO::new) // Convert each event
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EventBaseDTO> getAllBaseEventsAsDTO() {
        List<Event> events = eventRepo.findAllWithAdminAndRegistrations();
        return List.copyOf(eventAssembler.toDOList(events));
    }

    @Transactional
    public Event getEvent(UUID Id) {
        Event event = eventRepo.findByIdWithRegistrations(Id).orElse(null);
        return event;
    }

    public Event createEvent(EventDTO eventDTO, User admin) {
        Event event = new Event();
        event.setName(eventDTO.getName());
        event.setDescription(eventDTO.getDescription());
        event.setLocation(eventDTO.getLocation());
        event.setDate(eventDTO.getDate());
        event.setTime(eventDTO.getTime());
        event.setBanner(eventDTO.getBanner());
        event.setAdmin(admin);
        event.setParticipants(eventDTO.getParticipants());
        return eventRepo.save(event);
    }

    // TODO:Edit the updateEvent logic to not take the value from null field sent by user and add
    // the logic for json data that needs to be included...
    public Event updateEvent(UUID Id, Event event) {
        Event existingEvent = eventRepo.findById(Id).orElseGet(() -> eventRepo.save(event));
        existingEvent.setName(event.getName());
        existingEvent.setDescription(event.getDescription());
        existingEvent.setLocation(event.getLocation());
        existingEvent.setDate(event.getDate());
        existingEvent.setTime(event.getTime());
        existingEvent.setBanner(event.getBanner());
        existingEvent.setParticipants(event.getParticipants());
        return eventRepo.save(existingEvent);
    }

    public Event findById(UUID id) {
        return eventRepo.findById(id).orElse(null);
    }

    public void deleteEvent(Event event) {
        eventRegistrationRepo.deleteEvent(event);
        eventRepo.delete(event);
    }
}
