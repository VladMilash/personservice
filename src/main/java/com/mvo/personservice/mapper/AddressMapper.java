package com.mvo.personservice.mapper;

import com.mvo.personservice.entity.Address;
import dto.AddressDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressDTO map(Address address);

    @InheritInverseConfiguration
    Address map(AddressDTO addressDTO);
}
