package com.event_management.assembler;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

//HACK: to work on this...
import org.springframework.stereotype.Component;

import com.event_management.dto.EventBaseDTO;
import com.event_management.model.Event;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EventBaseAssembler {

    public EventBaseDTO toDTO(Event event) {
        if (event == null) {
            return null;  // Or throw IllegalArgumentException, depending on your requirements
        }

        EventBaseDTO dto = new EventBaseDTO();
        dto.setId(event.getId());
        dto.setName(event.getName());
        dto.setBadge(event.getBadge());
        dto.setDescription(event.getDescription());
        dto.setDate(event.getDate());
        dto.setTime(event.getTime());
        dto.setLocation(event.getLocation());
        
        if (event.getAdmin() != null) {
            dto.setAdminProfilePic(event.getAdmin().getProfilePic());
            dto.setAdminName(event.getAdmin().getName());
        }
        return dto;
    }

    public List<EventBaseDTO> toDTOList(List<Event> events) {
        if (events == null) {
            return Collections.emptyList();
    }
        return events.stream()
                .filter(Objects::nonNull)  // Filter out any null events
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
