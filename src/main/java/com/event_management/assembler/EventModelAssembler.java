package com.event_management.assembler;
// HACK: To work on this...

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.event_management.controller.EventController;
import com.event_management.dto.EventBaseDTO;
import com.event_management.model.Event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Component
public class EventModelAssembler implements RepresentationModelAssembler<EventBaseDTO, EntityModel<EventBaseDTO>> {
    @Override
    public EntityModel<EventBaseDTO> toModel(EventBaseDTO event) {
        return EntityModel.of(event,
            linkTo(methodOn(EventController.class).getEvent(event.getId())).withSelfRel(),
            linkTo(methodOn(EventController.class).getAllBaseEvents()).withRel("events")
        );
    }

    public EntityModel<Event> toModel(Event event) {
        return EntityModel.of(event,
            linkTo(methodOn(EventController.class).getEvent(event.getId())).withSelfRel(),
            linkTo(methodOn(EventController.class).getAllEvents()).withRel("events")
        );
    }

    public List<EventBaseDTO> toDOList(List<Event> events) {
        return events.stream()
            .map(event -> {
                EventBaseDTO dto = new EventBaseDTO();
                dto.setId(event.getId());
                dto.setName(event.getName());
                dto.setDescription(event.getDescription());
                dto.setLocation(event.getLocation());
                dto.setDate(event.getDate());
                dto.setTime(event.getTime());
                dto.setBanner(event.getBanner());
            
                if (event.getAdmin() != null) {
                    dto.setAdminName(event.getAdmin().getName());
                    dto.setAdminProfilePic(event.getAdmin().getProfilePic());
                }
            
                return dto;
            })
        .collect(Collectors.toList());
    }
}
