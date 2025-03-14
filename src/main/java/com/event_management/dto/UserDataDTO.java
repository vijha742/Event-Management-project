package com.event_management.dto;

import java.util.ArrayList;
import java.util.List;
import com.event_management.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDataDTO {
    private UserResponseDTO userData;
    private List<EventRegistrationSummaryDTO> eventRegistrations;

    public UserDataDTO(User user) {
	this.userData = new UserResponseDTO(user);
	this.eventRegistrations = new ArrayList<>();
    }
}

