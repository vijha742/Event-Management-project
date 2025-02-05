package com.event_management;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
class UserService {
	private final UserRepository userRepository;
	
	public User createUser(User user) {
		return userRepository.save(user);
	}
	
	public User getUser(int id) {
		return userRepository.findById(id).orElse(null);
	}
	
	public User updateUser(User user, int id) {
		User existingUser = userRepository.findById(id)
			.orElseGet(() -> userRepository.save(user));
		existingUser.setName(user.getName());
		existingUser.setEmail(user.getEmail());
		existingUser.setPassword(user.getPassword());
		existingUser.setRole(user.getRole());
		return userRepository.save(existingUser);
	}
	
	public void deleteUser(int id) {
		userRepository.deleteById(id);
	}
	
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}
}
