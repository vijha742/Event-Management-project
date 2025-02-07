package com.event_management;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "events")
@Builder
public class Event {
	@Column(nullable = false)
	private String name;
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private UUID id;
	private String description;
	private String location;
	@Column(nullable = false)
	private LocalDate date;
	@Column(nullable = false)
	private LocalTime time;
	private String banner;
	@ManyToOne
	@JoinColumn(name = "admin_id", nullable = false)
	private User admin;
	private int participants;

	@OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EventRegistration> eventRegistrations;
}	
