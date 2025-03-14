package com.event_management.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import  com.event_management.model.Event;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EventBaseDTO {

    private UUID id;
    private String name;
    private String badge;
    private String description;
    private LocalDate date;
    private LocalTime time;
    private String location;
    private String adminProfilePic;
    private String adminName;
    private String banner;

    public EventBaseDTO(Event event) {
        this.setId(event.getId());
        this.setName(event.getName());
	this.setBadge(event.getBadge());
        this.setDescription(event.getDescription());
        this.setLocation(event.getLocation());
        this.setDate(event.getDate());
        this.setTime(event.getTime());
        this.setBanner(event.getBanner());
        this.setAdminName(event.getAdmin().getName());
	this.setAdminProfilePic(event.getAdmin().getProfilePic());
    }
}
