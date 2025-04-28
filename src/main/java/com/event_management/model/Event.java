package com.event_management.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

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
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "events")
public class Event {
    @Column(nullable = false)
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    private String badge;
    private String description;
    private String location;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime time;

    @Column(columnDefinition = "text")
    private String banner;

    @Column(columnDefinition = "text")
    private String logo;

    private Set<String> socialLinks;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "authorized_users", columnDefinition = "jsonb")
    private List<Coordinators> authorized_users = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private User admin;

    private int participants;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "about_data", columnDefinition = "jsonb")
    private AboutData aboutData;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "announcements", columnDefinition = "jsonb")
    private List<Announcement> announcement;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "guidelines_data", columnDefinition = "jsonb")
    private GuidelinesData guidelinesData;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "faq_data", columnDefinition = "jsonb")
    private List<FaqItem> faqData;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "timeline_data", columnDefinition = "jsonb")
    private List<TimelineItem> timeline;

    @OneToMany(mappedBy = "event")
    @JsonManagedReference(value = "event-registration")
    private Set<EventRegistration> eventRegistrations;
}
