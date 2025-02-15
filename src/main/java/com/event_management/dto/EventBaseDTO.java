package com.event_management.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
