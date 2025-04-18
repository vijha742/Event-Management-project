package com.event_management.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
	public UserResponseDTO getUser(UUID id) {
		User user = userRepository.findById(id).orElse(null);
		UserResponseDTO userData = toUserReturnDTO(user);
		return userData;
	}

	public User updateUser(User user, UUID id) {
		User existingUser = userRepository.findById(id)
			.orElseGet(() -> userRepository.save(user));
		existingUser.setName(user.getName());
		existingUser.setEmail(user.getEmail());
		existingUser.setPassword(user.getPassword());
		existingUser.setRole(user.getRole());
		existingUser.setProfilePic(user.getProfilePic());
		existingUser.setPhoneNo(user.getPhoneNo());
		existingUser.setStudyYear(user.getStudyYear());
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
