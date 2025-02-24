package com.event_management.assembler;

import com.event_management.dto.UserResponseDTO;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.event_management.model.User;
import com.event_management.controller.UserController;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {

  @Override
  public EntityModel<User> toModel(User user) {

    return EntityModel.of(user, //
        linkTo(methodOn(UserController.class).getUser(user.getId())).withSelfRel(),       
		linkTo(methodOn(UserController.class).getAllUsers()).withRel("users"));
  }

  public EntityModel<UserResponseDTO> toModel(UserResponseDTO user) {

    return EntityModel.of(user, //
        linkTo(methodOn(UserController.class).getUser(user.getId())).withSelfRel(),       
		linkTo(methodOn(UserController.class).getAllUsersResponse()).withRel("users"));
  }
}
