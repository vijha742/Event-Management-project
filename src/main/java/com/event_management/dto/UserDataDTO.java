package com.event_management.dto;

import java.util.UUID;
import java.util.ArrayList;
import java.util.List;
import com.event_management.model.Role;
import com.event_management.model.User;
import java.time.LocalDateTime;

public class UserDataDTO {
    private UserResponseDTO user;
    private List<EventRegistrationSummaryDTO> eventRegistrations;

    public UserDataDTO(User user) {
	UserResponseDTO userData = new UserResponseDTO(user);
	List<EventRegistrationSummaryDTO> eventRegistrations = new ArrayList<>();
    }
}

