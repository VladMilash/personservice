package com.mvo.personservice.service.impl;

import com.mvo.personservice.entity.*;
import com.mvo.personservice.exception.EntityNotFoundException;
import com.mvo.personservice.repository.UserRepository;
import com.mvo.personservice.service.AddressService;
import com.mvo.personservice.service.CountryService;
import com.mvo.personservice.service.IndividualService;
import com.mvo.personservice.service.UserHistoryService;
import com.mvo.personservice.service.impl.util.UpdateEntityHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements com.mvo.personservice.service.UserService {
    private final UserRepository userRepository;
    private final UserHistoryService userHistoryService;
    private final AddressService addressService;
    private final CountryService countryService;
    private final IndividualService individualService;
    private final UpdateEntityHelper updateEntityHelper;

    @Override
    public Mono<User> createUser(User user) {
        return userRepository.save(user)
                .doOnSuccess(savedUser -> log.info("User with id {} has been saved successfully", savedUser.getId()))
                .doOnError(error -> log.error("Failed to saving user", error));
    }

    @Override
    public Mono<User> getById(UUID id) {
        return userRepository.findById(id)
                .doOnSuccess(user -> log.info("User with id {} has been found", id))
                .doOnError(error -> log.error("Failed to finding user with id {}", id, error));
    }

    @Override
    public Flux<User> getAll() {
        return userRepository.findAll()
                .doOnComplete(() -> log.info("Users has been found"))
                .doOnError(error -> log.error("Failed to finding users", error));
    }

    @Override
    public Mono<Address> getUserAddressByUserId(UUID id) {
        return getById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Not found user with this id", "NOT_FOUNDED_USER")))
                .flatMap(user -> addressService.getById(user.getAddressId()))
                .doOnSuccess(address -> log.info("Address with id {} has been found", address.getId()))
                .doOnError(error -> log.error("Failed to finding address with user id {}", id, error));

    }

    @Override
    public Mono<Country> getUserCountryByUserId(UUID id) {
        return getUserAddressByUserId(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Not found address for user with this id", "NOT_FOUNDED_ADDRESS")))
                .flatMap(address -> countryService.findById(address.getCountryId()))
                .doOnSuccess(country -> log.info("Country with id {} has been found", country.getId()))
                .doOnError(error -> log.error("Failed to finding country with user id {}", id, error));
    }

    @Override
    public Flux<Individual> getIndividualsByUserId(UUID id) {
        return getById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Not found user with this id", "NOT_FOUNDED_USER")))
                .flatMapMany(user -> individualService.findByUserId(id))
                .doOnComplete(() -> log.info("Operation for finding individuals by userId {} completed", id))
                .doOnError(error -> log.error("Failed to finding individuals by userId {}", id, error));
    }

    @Override
    public Mono<Void> deleteById(UUID id) {
        return userRepository.deleteById(id)
                .doOnSuccess(user -> log.info("Operation for user delete has finished successfully"))
                .doOnError(error -> log.error("Failed to delete user", error));
    }

    @Override
    public Mono<UserHistory> updateUser(User entity) {
        return userRepository.findById(entity.getId())
                .switchIfEmpty(Mono.error(new EntityNotFoundException("User not found", "USER_NOT_FOUND")))
                .flatMap(foundUser -> {
                    Map<String, Object> changedValues = new HashMap<>();
                    User updatedUser = updateUserFields(entity, foundUser, changedValues);
                    return createUser(updatedUser)
                            .then(Mono.just(updateEntityHelper.createUserHistoryEntity(updatedUser, updateEntityHelper.createJson(changedValues))))
                            .flatMap(userHistoryService::save);
                })
                .doOnSuccess(userHistory -> log.info("User with id {} has been updated successfully", entity.getId()))
                .doOnError(error -> log.error("Failed to update user with id {}", entity.getId(), error));

    }

    private User updateUserFields(User userFromRequestForUpdate, User userFoundedForUpdate,
                                  Map<String, Object> changedValues) {
        updateEntityHelper.checkAndUpdateFields(userFromRequestForUpdate::getSecretKey, userFoundedForUpdate::getSecretKey,
                "secretKey", userFoundedForUpdate::setSecretKey, changedValues);

        updateEntityHelper.checkAndUpdateFields(userFromRequestForUpdate::getFirstName, userFoundedForUpdate::getFirstName,
                "first_name", userFoundedForUpdate::setFirstName, changedValues);

        updateEntityHelper.checkAndUpdateFields(userFromRequestForUpdate::getLastName, userFoundedForUpdate::getLastName,
                "last_name", userFoundedForUpdate::setLastName, changedValues);

        updateEntityHelper.checkAndUpdateLocalDateTimeFields(userFromRequestForUpdate::getVerifiedAt, userFoundedForUpdate::getVerifiedAt,
                "verified_at", userFoundedForUpdate::setVerifiedAt, changedValues);

        updateEntityHelper.checkAndUpdateLocalDateTimeFields(userFromRequestForUpdate::getArchivedAt, userFoundedForUpdate::getArchivedAt,
                "archived_at", userFoundedForUpdate::setArchivedAt, changedValues);

        updateEntityHelper.checkAndUpdateFields(userFromRequestForUpdate::getStatus, userFoundedForUpdate::getStatus,
                "status", userFoundedForUpdate::setStatus, changedValues);

        updateEntityHelper.checkAndUpdateFields(userFromRequestForUpdate::getFilled, userFoundedForUpdate::getFilled,
                "filled", userFoundedForUpdate::setFilled, changedValues);

        return userFoundedForUpdate;
    }
}
