package com.mvo.personservice.service.impl;

import com.mvo.personservice.entity.Individual;
import com.mvo.personservice.repository.IndividualRepository;
import com.mvo.personservice.service.IndividualService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
@Service
public class IndividualServiceImpl implements IndividualService {
    private final IndividualRepository individualRepository;

    @Override
    public Mono<Individual> createIndividual(Individual individual) {
        return individualRepository.save(individual)
                .doOnSuccess(savedIndividual -> log.info("Individual with id {} has been saved successfully", savedIndividual.getId()))
                .doOnError(error -> log.error("Failed to saving individual", error));
    }

    @Override
    public Mono<Individual> findByEmail(String email) {
        return individualRepository.findByEmail(email)
                .doOnSuccess(individual -> log.info("Operation for founding has finished successfully"))
                .doOnError(error -> log.error("Failed to founding individual", error));
    }
}
