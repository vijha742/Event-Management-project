package com.event_management.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.event_management.controller.UserController;
import com.event_management.dto.UserDataDTO;

@Component
public class UserDataDTOModelAssembler implements RepresentationModelAssembler<UserDataDTO, EntityModel<UserDataDTO>> {
    @Override
    public EntityModel<UserDataDTO> toModel(UserDataDTO user) {

    return EntityModel.of(user, //
        linkTo(methodOn(UserController.class).getUser(user.getUserData().getId())).withSelfRel());
  }
}

