package com.event_management.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Announcement {
		private String title;
		private String description;
		private LocalDate createdAt = LocalDate.now();
		private LocalDateTime deadline;
}
