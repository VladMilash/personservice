package com.mvo.personservice.service;

import com.mvo.personservice.entity.Individual;
import com.mvo.personservice.entity.UserHistory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface IndividualService {
    Mono<Individual> createIndividual(Individual individual);

    Mono<Individual> findByEmail(String email);

    Flux<Individual> findByUserId(UUID id);

    Mono<UserHistory> updateIndividual(Individual individual);

    Mono<Individual> findByPassportNumber(String passportNumber);

    Mono<Void> deleteById(UUID id);

}
