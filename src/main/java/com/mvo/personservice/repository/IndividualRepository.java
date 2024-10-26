package com.mvo.personservice.repository;

import com.mvo.personservice.entity.Individual;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface IndividualRepository extends R2dbcRepository<Individual, UUID> {
    @Query("SELECT * FROM individuals WHERE email = :email")
    Mono<Individual> findByEmail(String email);

   Mono<Individual> findByUserId(UUID id);

    Mono<Individual> findByPassportNumber(String passportNumber);

}
