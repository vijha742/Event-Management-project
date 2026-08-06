package com.event_management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;
import com.event_management.model.Role;
import com.event_management.model.User;

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

		public UserResponseDTO(User user) {
			this.id = user.getId();
			this.name = user.getName();
			this.email = user.getEmail();
			this.profilePic = user.getProfilePic();
			this.phoneNo = user.getPhoneNo();
			this.rollNo = user.getRollNo();
			this.studyYear = user.getStudyYear();
			this.department = user.getDepartment();
			this.role = user.getRole();
	}
}
