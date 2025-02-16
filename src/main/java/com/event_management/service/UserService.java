package com.event_management.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	public User getUser(UUID id) {
		return userRepository.findById(id).orElse(null);
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
	
	public void deleteUser(UUID id) {
		userRepository.deleteById(id);
	}
	
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}
}
