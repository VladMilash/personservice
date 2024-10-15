package com.mvo.personservice.mapper;

import com.mvo.personservice.entity.Individual;
import dto.IndividualDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IndividualMapper {
    IndividualDTO map(Individual individual);

    @InheritInverseConfiguration
    Individual map(IndividualDTO individualDTO);
}
