package com.event_management.service;

import com.event_management.dto.UserResponseDTO;
import com.event_management.model.Role;
import com.event_management.model.User;
import com.event_management.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User createUserwithOAuth(OAuth2User user) {
        String email = user.getAttribute("email");
        User preUser = userRepository.findByEmail("email").orElse(null);

        if (preUser == null) {
            preUser = new User();
            preUser.setEmail(email);
            preUser.setName(user.getAttribute("name"));
            preUser.setRole(Role.USER);
        }
        return userRepository.save(preUser);
    }

    @Transactional
    public UserResponseDTO getUser(UUID id) {
        User user = userRepository.findById(id).orElse(null);
        UserResponseDTO userData = toUserReturnDTO(user);
        return userData;
    }

    // TODO: How'd you fetch the user's roll no. and department?
    public User updateUser(UserResponseDTO user, UUID id) {
        User existingUser = userRepository.findById(id).orElseGet(() -> new User());
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        //        existingUser.setPassword(user.getPassword());
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
        List<UserResponseDTO> responses =
                user.stream().map(this::toUserReturnDTO).collect(Collectors.toList());
        return responses;
    }

    public UserResponseDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        return toUserReturnDTO(user);
    }

    public boolean isUserExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
