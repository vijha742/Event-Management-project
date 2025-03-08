package com.event_management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;
import com.event_management.model.Role;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserResponseDTO {

		private UUID id;
		private String name;
		private String email;
		private String profilePic;
		private String phoneNo;
		private String rollNo;
		private int studyYear;
		private String department;
		private Role role;
}
