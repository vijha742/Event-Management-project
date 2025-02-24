package com.event_management.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "events")
public class Event {
	@Column(nullable = false)
	private String name;
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private UUID id;

	private String badge;
	private String description;
	private String location;

	@Column(nullable = false)
	private LocalDate date;
	
	@Column(nullable = false)
	private LocalTime time;
	
	private String banner;
	private String logo;
	private Set<String> socialLinks;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "authorized_users", columnDefinition = "jsonb")
	private List<Coordinators> authorized_users;

	@ManyToOne
	@JoinColumn(name = "admin_id", nullable = false)
	private User admin;

	private int participants;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "about_data", columnDefinition = "jsonb")
	private AboutData aboutData;
    
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "guidelines_data", columnDefinition = "jsonb")
	private GuidelinesData guidelinesData;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "faq_data", columnDefinition = "jsonb")
	private List<FaqItem> faqData;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "timeline_data", columnDefinition = "jsonb")
	private List<TimelineItem> timeline;

	@OneToMany(mappedBy = "event", fetch =FetchType.LAZY)
	private Set<EventRegistration> eventRegistrations;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class AboutData {
    private String title;
    private List<AboutSection> sections;
    private CallToAction callToAction;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class AboutSection {
    private String title;
    private String content;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class CallToAction {
    private String title;
    private String content;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class GuidelinesData {
    @JsonProperty("generalRules")
    private List<String> generalRules;
    
    @JsonProperty("technicalGuidelines")
    private List<String> technicalGuidelines;
    
    @JsonProperty("judgingCriteria")
    private List<String> judgingCriteria;
    
    private ResourceInfo resources;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ResourceInfo {
    private String text;
    private String link;
    private String linkText;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class TimelineItem {
    private String title;
    private String time;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class FaqItem {
    private String question;
    private String answer;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Coordinators {
    private UUID id;
    private String role;
}

