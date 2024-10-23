package com.mvo.personservice.service.impl;

import com.mvo.personservice.entity.Individual;
import com.mvo.personservice.entity.UserHistory;
import com.mvo.personservice.exception.EntityNotFoundException;
import com.mvo.personservice.repository.IndividualRepository;
import com.mvo.personservice.service.IndividualService;
import com.mvo.personservice.service.UserService;
import com.mvo.personservice.service.impl.util.UpdateEntityHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Slf4j
@Service
public class IndividualServiceImpl implements IndividualService {
    private final IndividualRepository individualRepository;
    private final UpdateEntityHelper updateEntityHelper;
    private final UserService userService;

    public IndividualServiceImpl(IndividualRepository individualRepository,
                                 UpdateEntityHelper updateEntityHelper,
                                 @Lazy UserService userService) {
        this.individualRepository = individualRepository;
        this.updateEntityHelper = updateEntityHelper;
        this.userService = userService;
    }

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

    @Override
    public Flux<Individual> findByUserId(UUID id) {
        return individualRepository.findByUserId(id)
                .doOnComplete(() -> log.info("Operation for finding individuals by userId {} has finished successfully", id))
                .doOnError(error -> log.error("Failed to founding individual", error));
    }

    @Override
    public Mono<UserHistory> updateIndividual(Individual individual) {
        return userService.getById(individual.getUserId())
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Not found user with this id", "NOT_FOUNDED_USER")))
                .flatMap(user -> individualRepository.findById(individual.getId())
                        .switchIfEmpty(Mono.error(new EntityNotFoundException("Not found individual with this id", "NOT_FOUNDED_INDIVIDUAL")))
                        .flatMap(foundedAddress -> {
                            Map<String, Object> changedValues = new HashMap<>();
                            Individual upadetedIndividual = updateIndividualField(individual, foundedAddress, changedValues);
                            return createIndividual(upadetedIndividual)
                                    .then(Mono.just(updateEntityHelper.createUserHistoryEntity(user, updateEntityHelper.createJson(changedValues))));
                        }))
                .doOnSuccess(userHistory -> log.info("Individual for user user with id {} has been updated successfully", individual.getUserId()))
                .doOnError(error -> log.error("Failed to update  individual for user with id {}", individual.getUserId(), error));
    }

    private Individual updateIndividualField(Individual individualFromRequestForUpdate, Individual individualFoundedForUpdate,
                                             Map<String, Object> changedValues) {
        updateEntityHelper.checkAndUpdateFields(individualFromRequestForUpdate::getPassportNumber, individualFoundedForUpdate::getPassportNumber,
                "passport_number", individualFoundedForUpdate::setPassportNumber, changedValues);

        updateEntityHelper.checkAndUpdateFields(individualFromRequestForUpdate::getPhoneNumber, individualFoundedForUpdate::getPhoneNumber,
                "phone_number", individualFoundedForUpdate::setPhoneNumber, changedValues);

        updateEntityHelper.checkAndUpdateFields(individualFromRequestForUpdate::getEmail, individualFoundedForUpdate::getEmail,
                "email", individualFoundedForUpdate::setEmail, changedValues);

        updateEntityHelper.checkAndUpdateLocalDateTimeFields(individualFromRequestForUpdate::getVerifiedAt, individualFoundedForUpdate::getVerifiedAt,
                "verified_at", individualFoundedForUpdate::setVerifiedAt, changedValues);

        updateEntityHelper.checkAndUpdateLocalDateTimeFields(individualFromRequestForUpdate::getArchivedAt, individualFoundedForUpdate::getArchivedAt,
                "archived_at", individualFoundedForUpdate::setArchivedAt, changedValues);

        updateEntityHelper.checkAndUpdateFields(individualFromRequestForUpdate::getStatus, individualFoundedForUpdate::getStatus,
                "status", individualFoundedForUpdate::setStatus, changedValues);

        return individualFoundedForUpdate;

    }
}
