package com.event_management.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

import com.event_management.service.UserService;
import com.event_management.assembler.UserModelAssembler;
import com.event_management.dto.UserResponseDTO;
import com.event_management.model.User;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Validated
public class UserController {
	private final UserService userservice;
	private final UserModelAssembler assembler;		
	
	@GetMapping
	public CollectionModel<EntityModel<User>> getAllUsers() {
		List<User> base = userservice.getAllUsers();
		List<EntityModel<User>> users = base.stream()
																		.map(assembler::toModel) 
																		.collect(Collectors.toList());

		return CollectionModel.of(users, linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
	}
	@GetMapping
	public CollectionModel<EntityModel<UserResponseDTO>> getAllUsersResponse() {
		List<UserResponseDTO> base = userservice.getAllUsersResponse();
		List<EntityModel<UserResponseDTO>> users = base.stream()
																		.map(assembler::toModel) 
																		.collect(Collectors.toList());

		return CollectionModel.of(users, linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
	}

	@PostMapping
	public ResponseEntity<?> newUser(@RequestBody User newUser) {
		EntityModel<User> entityModel = assembler.toModel(userservice.createUser(newUser));
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}

	@GetMapping("/{id}")
	public EntityModel<User> getUser(@PathVariable UUID id) {
		User user = userservice.getUser(id);
		return assembler.toModel(user);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
		userservice.deleteUser(id);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> replaceUser(@RequestBody User newUser, @PathVariable UUID id) {
		User updatedUser = userservice.updateUser(newUser, id);
		EntityModel<User> entityModel = assembler.toModel(updatedUser);
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}
}
