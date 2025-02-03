package com.event_management;

import java.util.List;
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

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Validated
class UserController {
	private final UserService userservice;
	private final UserModelAssembler assembler;		
	
	@GetMapping
	CollectionModel<EntityModel<User>> getAllUsers() {
	List<EntityModel<User>> users = userservice.getAllUsers().stream()
		.map(assembler::toModel) 
		.collect(Collectors.toList());
		return CollectionModel.of(users, linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
	}

	@PostMapping
	ResponseEntity<?> newUser(@RequestBody User newUser) {
		EntityModel<User> entityModel = assembler.toModel(userservice.createUser(newUser));
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}

	@GetMapping("/{id}")
	public EntityModel<User> getUser(@PathVariable int id) {
		User user = userservice.getUser(id);
		return assembler.toModel(user);
	}

	@DeleteMapping("/{id}")
	ResponseEntity<?> deleteUser(@PathVariable int id) {
		userservice.deleteUser(id);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{id}")
	ResponseEntity<?> replaceUser(@RequestBody User newUser, @PathVariable int id) {
		User updatedUser = userservice.updateUser(newUser, id);
		EntityModel<User> entityModel = assembler.toModel(updatedUser);
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}
}
