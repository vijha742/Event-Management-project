package com.event_management;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
	private UUID id;
	private String description;
	private String location;
	private LocalDate date;
	private LocalTime time;
	private String banner;
//	private User organizer;
	private int participants;

	@OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EventRegistration> eventRegistrations;
}	
