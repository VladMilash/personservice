package com.mvo.personservice.repository;

import com.mvo.personservice.entity.Country;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface CountryRepository extends R2dbcRepository<Country, Integer> {
}
