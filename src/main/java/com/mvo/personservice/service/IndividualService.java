package com.mvo.personservice.service;

import com.mvo.personservice.entity.Individual;
import com.mvo.personservice.entity.User;
import reactor.core.publisher.Mono;

public interface IndividualService {
    Mono<Individual> createIndividual(Individual individual);

    Mono<Individual> findByEmail(String email);

}
