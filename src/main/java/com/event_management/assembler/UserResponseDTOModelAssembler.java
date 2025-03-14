package com.event_management.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.event_management.controller.UserController;
import com.event_management.dto.UserResponseDTO;

@Component
public class UserResponseDTOModelAssembler implements RepresentationModelAssembler<UserResponseDTO, EntityModel<UserResponseDTO>> {
    @Override
    public EntityModel<UserResponseDTO> toModel(UserResponseDTO user) {

    return EntityModel.of(user, //
        linkTo(methodOn(UserController.class).getUser(user.getId())).withSelfRel(),       linkTo(methodOn(UserController.class).getAllUsersResponse()).withRel("users"));
  }
}
