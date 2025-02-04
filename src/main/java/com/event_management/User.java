package com.event_management;

import java.util.Set;
import java.util.UUID;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.persistence.CascadeType;

@AllArgsConstructor
@Data
@Entity
@Table(name = "users")
class User {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private UUID id;
	private String name;
	private String email;
	private String password;
	private String role;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EventRegistration> eventRegistrations;
}
