package com.event_management.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.event_management.model.Event;
import com.event_management.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EventResponseDTO {
	private String name;
	private UUID id;
	private String badge;
	private String description;
	private String location;
	private LocalDate date;
	private LocalTime time;
	private String banner;
	private String logo;
	private Set<String> socialLinks;
	private List<Coordinators> authorized_users;
	private User admin;
	private int participants;
	private AboutData aboutData;
	private GuidelinesData guidelinesData;
	private List<FaqItem> faqData;
	private List<TimelineItem> timeline;
	private Set<RegistrationEventDTO> eventRegistrations;

	public EventResponseDTO(Event event) {
	    this.name = event.getName();
	    this.id = event.getId();
	    this.badge = event.getBadge();
	    this.description = event.getDescription();
	    this.location = event.getLocation();
	    this.date = event.getDate();
	    this.time = event.getTime();
	    this.banner = event.getBanner();
	    this.logo = event.getLogo();
	    this.socialLinks = event.getSocialLinks();
	    this.authorized_users = event.getAuthorized_users() != null 
        ? event.getAuthorized_users().stream()
					.map(this::mapCoordinators)
					.collect(Collectors.toList()) : new ArrayList<>();
	    this.admin = event.getAdmin();
	    this.participants = event.getParticipants();
	    this.aboutData = event.getAboutData() != null ? mapAboutData(event.getAboutData()) : new AboutData();
	    this.guidelinesData = event.getGuidelinesData() != null ? mapGuidelinesData(event.getGuidelinesData()) : new GuidelinesData();
	    this.faqData = event.getFaqData() != null ? event.getFaqData().stream()
				.map(this::mapFaqItem)
				.collect(Collectors.toList()) : new ArrayList<>();
	    this.timeline = event.getTimeline() != null ? event.getTimeline().stream()
        .map(this::mapTimelineItem)
        .collect(Collectors.toList()) : new ArrayList<>();
	    this.eventRegistrations = event.getEventRegistrations() != null ? event.getEventRegistrations().stream()
	.map(registration -> new RegistrationEventDTO(registration)).collect(Collectors.toSet()) : new HashSet<>();

	}

    private Coordinators mapCoordinators(com.event_management.model.Coordinators modelCoordinator) {
	Coordinators dtoCoordinator = new Coordinators();
	dtoCoordinator.setId(modelCoordinator.getId());
	dtoCoordinator.setRole(modelCoordinator.getRole());
	return dtoCoordinator;
    }

// Helper method to map AboutData
    private AboutData mapAboutData(com.event_management.model.AboutData modelAboutData) {
	AboutData dtoAboutData = new AboutData();
	dtoAboutData.setTitle(modelAboutData.getTitle());
    
    // Map AboutSections
	dtoAboutData.setSections(modelAboutData.getSections().stream()
	    .map(this::mapAboutSection)
	    .collect(Collectors.toList()));
    
    // Map CallToAction
	dtoAboutData.setCallToAction(mapCallToAction(modelAboutData.getCallToAction()));
    
	return dtoAboutData;
    }

// Helper method to map AboutSection
    private AboutSection mapAboutSection(com.event_management.model.AboutSection modelSection) {
	AboutSection dtoSection = new AboutSection();
	dtoSection.setTitle(modelSection.getTitle());
	dtoSection.setContent(modelSection.getContent());
	return dtoSection;
    }

// Helper method to map CallToAction
    private CallToAction mapCallToAction(com.event_management.model.CallToAction modelCallToAction) {
	CallToAction dtoCallToAction = new CallToAction();
	dtoCallToAction.setTitle(modelCallToAction.getTitle());
	dtoCallToAction.setContent(modelCallToAction.getContent());
	return dtoCallToAction;
    }

// Helper method to map GuidelinesData
    private GuidelinesData mapGuidelinesData(com.event_management.model.GuidelinesData modelGuidelinesData) {
	GuidelinesData dtoGuidelinesData = new GuidelinesData();
	dtoGuidelinesData.setGeneralRules(modelGuidelinesData.getGeneralRules());
	dtoGuidelinesData.setTechnicalGuidelines(modelGuidelinesData.getTechnicalGuidelines());
	dtoGuidelinesData.setJudgingCriteria(modelGuidelinesData.getJudgingCriteria());
    
    // Map ResourceInfo
	dtoGuidelinesData.setResources(mapResourceInfo(modelGuidelinesData.getResources()));
    
	return dtoGuidelinesData;
    }

// Helper method to map ResourceInfo
    private ResourceInfo mapResourceInfo(com.event_management.model.ResourceInfo modelResourceInfo) {
	ResourceInfo dtoResourceInfo = new ResourceInfo();
	dtoResourceInfo.setText(modelResourceInfo.getText());
	dtoResourceInfo.setLink(modelResourceInfo.getLink());
	dtoResourceInfo.setLinkText(modelResourceInfo.getLinkText());
	return dtoResourceInfo;
    }

// Helper method to map FaqItem
    private FaqItem mapFaqItem(com.event_management.model.FaqItem modelFaqItem) {
	FaqItem dtoFaqItem = new FaqItem();
	dtoFaqItem.setQuestion(modelFaqItem.getQuestion());
	dtoFaqItem.setAnswer(modelFaqItem.getAnswer());
	return dtoFaqItem;
    }

// Helper method to map TimelineItem
    private TimelineItem mapTimelineItem(com.event_management.model.TimelineItem modelTimelineItem) {
	TimelineItem dtoTimelineItem = new TimelineItem();
	dtoTimelineItem.setTitle(modelTimelineItem.getTitle());
	dtoTimelineItem.setTime(modelTimelineItem.getTime());
	return dtoTimelineItem;
    }



@Data
@NoArgsConstructor
@AllArgsConstructor
class AboutData {
    private String title;
    private List<AboutSection> sections = new ArrayList<>();
    private CallToAction callToAction = new CallToAction();
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
    private List<String> generalRules = new ArrayList<>();
    
    @JsonProperty("technicalGuidelines")
    private List<String> technicalGuidelines = new ArrayList<>();
    
    @JsonProperty("judgingCriteria")
    private List<String> judgingCriteria = new ArrayList<>();
    
    private ResourceInfo resources = new ResourceInfo();
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


}


