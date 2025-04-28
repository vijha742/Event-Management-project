package com.event_management.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.event_management.assembler.UserModelAssembler;
import com.event_management.assembler.UserResponseDTOModelAssembler;
import com.event_management.dto.UserResponseDTO;
import com.event_management.model.User;
import com.event_management.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userservice;
    private final UserModelAssembler assembler;
    private final UserResponseDTOModelAssembler responseAssembler;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/find-user")
    public String getUserName(@AuthenticationPrincipal OAuth2User principal) {
        String email = principal.getAttribute("email");
        return email;
    }

    @GetMapping("/user")
    public OAuth2User getUser(@AuthenticationPrincipal OAuth2User principal) {
        return principal;
    }

    @GetMapping("/c")
    public UserResponseDTO getCurrentUserData() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new RuntimeException("No authentication found. User is not logged in.");
        }
        String userEmail;
        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
            userEmail = oauth2User.getAttribute("email");
        } else {
            userEmail = authentication.getName();
        }

        UserResponseDTO userOptional = userservice.getUserByEmail(userEmail);

        return userOptional;
    }

    @GetMapping
    public CollectionModel<EntityModel<User>> getAllUsers() {
        List<User> base = userservice.getAllUsers();
        List<EntityModel<User>> users =
                base.stream().map(assembler::toModel).collect(Collectors.toList());

        return CollectionModel.of(
                users, linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
    }

    @GetMapping("/base")
    public CollectionModel<EntityModel<UserResponseDTO>> getAllUsersResponse() {
        List<UserResponseDTO> base = userservice.getAllUsersResponse();
        List<EntityModel<UserResponseDTO>> users =
                base.stream().map(responseAssembler::toModel).collect(Collectors.toList());

        return CollectionModel.of(
                users, linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
    }

    @PostMapping
    public ResponseEntity<?> newUser(@RequestBody User newUser) {
        EntityModel<User> entityModel = assembler.toModel(userservice.createUser(newUser));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/{id}")
    public EntityModel<UserResponseDTO> getUser(@PathVariable UUID id) {
        UserResponseDTO user = userservice.getUser(id);
        return responseAssembler.toModel(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        UserResponseDTO targetUser = userservice.getUser(id);

        if (getCurrentUserData().getRole().equals("ADMIN")
                || getCurrentUserData().getId().equals(id)) {
            userservice.deleteUser(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You don't have permission to delete this user account");
        }
    }

    /*    @PutMapping("/{id}")
        public ResponseEntity<?> replaceUser(
                @RequestBody UserResponseDTO newUser, @PathVariable UUID id) {
            if (getCurrentUserData().getRole().equals("ADMIN")
                    || getCurrentUserData().getId().equals(id)) {
                User updatedUser = userservice.updateUser(newUser, id);
                EntityModel<User> entityModel = assembler.toModel(updatedUser);
                return ResponseEntity.created(
                                entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                        .body(entityModel);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("You don't have permission to update this user account");
            }
        }
    */
    @PutMapping("/{id}")
    public ResponseEntity<?> replaceUser(
            @RequestBody UserResponseDTO newUser,
            @PathVariable UUID id,
            HttpServletRequest request) {
        // Debug request information
        // Debug authentication information
        logger.debug("Request to update user with ID: {}", id);
        logger.debug(
                "Current authentication principal: {}",
                SecurityContextHolder.getContext().getAuthentication());

        logger.debug(
                "Current authentication principal: {}",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        // Debug cookies
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                logger.debug("Cookie: {} = {}", cookie.getName(), "present");
            }
        } else {
            logger.debug("No cookies found in request");
        }

        // Debug headers
        logger.debug("XSRF Token header: {}", request.getHeader("X-XSRF-TOKEN"));

        // Debug authorization check
        UserResponseDTO currentUser = getCurrentUserData();
        logger.debug("Current user role: {}", currentUser.getRole());
        logger.debug("Current user ID: {}", currentUser.getId());
        logger.debug("Is user admin? {}", currentUser.getRole().equals("ADMIN"));
        logger.debug("Is user the same? {}", currentUser.getId().equals(id));

        if (currentUser.getRole().equals("ADMIN") || currentUser.getId().equals(id)) {
            logger.debug("Authorization passed - processing update");
            User updatedUser = userservice.updateUser(newUser, id);
            EntityModel<User> entityModel = assembler.toModel(updatedUser);
            return ResponseEntity.created(
                            entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                    .body(entityModel);
        } else {
            logger.debug("Authorization failed - returning 403");
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You don't have permission to update this user account");
        }
    }
}
