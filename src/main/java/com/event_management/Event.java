package com.event_management;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "events")
public class Event {
	private String name;
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;
	private String description;
	private String location;
	private LocalDate date;
	private LocalTime time;
	private String banner;
//	private User organizer;
	private int participants;
}	
