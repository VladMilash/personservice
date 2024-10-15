package com.mvo.personservice.mapper;

import com.mvo.personservice.entity.Country;
import dto.CountryDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CountryMapper {
    CountryDTO map(Country country);

    @InheritInverseConfiguration
    Country map(CountryDTO countryDTO);
}
