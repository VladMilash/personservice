package com.mvo.personservice.service;

import com.mvo.personservice.entity.Individual;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface IndividualService {
    Mono<Individual> createIndividual(Individual individual);

    Mono<Individual> findByEmail(String email);

    Flux<Individual> findByUserId(UUID id);

}
