package io.github.vincemann.springrapid.demo.controllers;

import io.github.vincemann.springrapid.demo.dtos.pet.BasePetDto;
import io.github.vincemann.springrapid.demo.dtos.pet.UpdatePetDto;
import io.github.vincemann.springrapid.demo.model.Pet;
import io.github.vincemann.springrapid.core.config.layers.component.WebController;
import io.github.vincemann.springrapid.core.controller.dtoMapper.context.Direction;
import io.github.vincemann.springrapid.core.controller.dtoMapper.context.DtoMappingContextBuilder;
import io.github.vincemann.springrapid.core.controller.dtoMapper.context.CrudDtoEndpoint;
import io.github.vincemann.springrapid.core.controller.springAdapter.SpringAdapterJsonDtoCrudController;


@WebController
public class PetController
        extends SpringAdapterJsonDtoCrudController<Pet, Long> {

    public PetController() {
        super(DtoMappingContextBuilder.builder()
                .forAll(BasePetDto.class)
                .forEndpoint(CrudDtoEndpoint.PARTIAL_UPDATE, Direction.REQUEST, UpdatePetDto.class)
                .build()
        );
    }
}
