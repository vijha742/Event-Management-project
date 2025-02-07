package com.event_management;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EventDTO {
    private String name;
    private String description;
    private String location;
    private LocalDate date;
    private LocalTime time;
    private String banner;
    private UUID admin; // ✅ Only UUID instead of full User object
    private int participants;
}

