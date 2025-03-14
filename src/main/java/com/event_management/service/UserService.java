package com.event_management.service;

import java.util.List;
import java.util.UUID;

import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.event_management.dto.UserDataDTO;
import com.event_management.dto.UserResponseDTO;
import com.event_management.model.User;
import com.event_management.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	
public User createUser(User user) {
		return userRepository.save(user);
	}

	@Transactional
	public UserDataDTO getUser(UUID id) {
		User user = userRepository.findById(id).orElse(null);
		UserDataDTO userData = new UserDataDTO(user);
		userData.setEventRegistrations(user.getEventRegistrations().stream()
        .map(reg -> {
            EventRegistrationSummaryDTO regDto = new EventRegistrationSummaryDTO();
            
            EventSummaryDTO eventDto = new EventSummaryDTO();
            // Map basic event fields
            regDto.setid(reg.getId());
						regDto.setStatus(reg.getStatus());
						regDto.setRole(reg.getRole());
						regDto.setRegisteredAt(reg.getRegisteredAt());
						regDto.setEvent(new EventBaseDTO(reg.getEvent()));
            return regDto;
        })
        .collect(Collectors.toList()));
		return userData;
	}

	public User updateUser(User user, UUID id) {
		User existingUser = userRepository.findById(id)
			.orElseGet(() -> userRepository.save(user));
		existingUser.setName(user.getName());
		existingUser.setEmail(user.getEmail());
		existingUser.setPassword(user.getPassword());
		existingUser.setRole(user.getRole());
		return userRepository.save(existingUser);
	}

	public UserResponseDTO toUserReturnDTO(User user) {
		UserResponseDTO userResponse = new UserResponseDTO();
			userResponse.setId(user.getId());
			userResponse.setName(user.getName());
			userResponse.setEmail(user.getEmail());
			userResponse.setProfilePic(user.getProfilePic());
			userResponse.setPhoneNo(user.getPhoneNo());
			userResponse.setRollNo(user.getRollNo());
			userResponse.setStudyYear(user.getStudyYear());
			userResponse.setDepartment(user.getDepartment());
			userResponse.setRole(user.getRole());
			return userResponse;
	}
	
	public void deleteUser(UUID id) {
		userRepository.deleteById(id);
	}
	
	public List<User> getAllUsers() {
		return userRepository.findAllWithAdminAndRegistrations();
	}
	@Transactional(readOnly = true)
	public List<UserResponseDTO> getAllUsersResponse() {
		List<User> user = userRepository.findAllWithAdminAndRegistrations();
		List<UserResponseDTO> responses = user.stream().map(this::toUserReturnDTO).collect(Collectors.toList());
		return responses;
	}
}
