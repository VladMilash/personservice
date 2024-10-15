package com.mvo.personservice.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import java.util.UUID;

public interface Individual extends R2dbcRepository<Individual, UUID> {
}
