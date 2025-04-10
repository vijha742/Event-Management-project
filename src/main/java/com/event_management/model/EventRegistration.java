package com.event_management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "event_registrations")
public class EventRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference(value = "user-registration")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id", nullable = false)
    @JsonBackReference(value = "event-registration")
    private Event event;

    @Column(name = "registered_at", nullable = false)
    private LocalDateTime registeredAt = LocalDateTime.now();

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private RegistrationStatus status = RegistrationStatus.UPCOMING;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

}

