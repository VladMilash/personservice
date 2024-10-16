package com.mvo.personservice.repository;

import com.mvo.personservice.entity.Individual;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface IndividualRepository extends R2dbcRepository<Individual, UUID> {
    Mono<Individual> findByEmail(String email);
}
