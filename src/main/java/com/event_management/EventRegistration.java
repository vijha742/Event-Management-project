package com.event_management;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "event_registrations")
public class EventRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(name = "registered_at", nullable = false)
    private LocalDateTime registeredAt = LocalDateTime.now();

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RegistrationStatus status = RegistrationStatus.UPCOMING;

}

