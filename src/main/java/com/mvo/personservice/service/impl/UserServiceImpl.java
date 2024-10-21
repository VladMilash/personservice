package com.mvo.personservice.service.impl;

import com.mvo.personservice.entity.*;
import com.mvo.personservice.exception.EntityNotFoundException;
import com.mvo.personservice.repository.UserRepository;
import com.mvo.personservice.service.AddressService;
import com.mvo.personservice.service.CountryService;
import com.mvo.personservice.service.IndividualService;
import com.mvo.personservice.service.UserHistoryService;
import io.r2dbc.postgresql.codec.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements com.mvo.personservice.service.UserService {
    private final UserRepository userRepository;
    private final UserHistoryService userHistoryService;
    private final AddressService addressService;
    private final CountryService countryService;
    private final IndividualService individualService;

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
    public Mono<UserHistory> updateUser(User entity) {
        return userRepository.findById(entity.getId())
                .switchIfEmpty(Mono.error(new EntityNotFoundException("User not found", "USER_NOT_FOUND")))
                .flatMap(foundUser -> {
                    Map<String, Object> changedValues = new HashMap<>();
                    User updatedUser = updateUserFields(entity, foundUser, changedValues);
                    return createUser(updatedUser)
                            .then(Mono.just(createUserHistoryEntity(updatedUser, createJson(changedValues))))
                            .flatMap(userHistoryService::save);
                })
                .doOnSuccess(userHistory -> log.info("User with id {} has been updated successfully", entity.getId()))
                .doOnError(error -> log.error("Failed to update user with id {}", entity.getId(), error));

    }

    private User updateUserFields(User userFromRequestForUpdate, User userFoundedForUpdate, Map<String, Object> changedValues) {
        checkAndUpdateFields(userFromRequestForUpdate::getSecretKey, userFoundedForUpdate::getSecretKey,
                "SecretKey", userFoundedForUpdate::setSecretKey, changedValues);

        checkAndUpdateFields(userFromRequestForUpdate::getFirstName, userFoundedForUpdate::getFirstName,
                "First_name", userFoundedForUpdate::setFirstName, changedValues);

        checkAndUpdateFields(userFromRequestForUpdate::getLastName, userFoundedForUpdate::getLastName,
                "Last_name", userFoundedForUpdate::setLastName, changedValues);

        checkAndUpdateLocalDateTimeFields(userFromRequestForUpdate::getVerifiedAt, userFoundedForUpdate::getVerifiedAt,
                "Verified_at", userFoundedForUpdate::setVerifiedAt, changedValues);

        checkAndUpdateLocalDateTimeFields(userFromRequestForUpdate::getArchivedAt, userFoundedForUpdate::getArchivedAt,
                "Archived_at", userFoundedForUpdate::setArchivedAt, changedValues);

        checkAndUpdateFields(userFromRequestForUpdate::getStatus, userFoundedForUpdate::getStatus,
                "Status", userFoundedForUpdate::setStatus, changedValues);

        checkAndUpdateFields(userFromRequestForUpdate::getFilled, userFoundedForUpdate::getFilled,
                "Filled", userFoundedForUpdate::setFilled, changedValues);

        return userFoundedForUpdate;
    }

    private <T> void checkAndUpdateFields(Supplier<T> fieldGetterFromUserFromRequest, Supplier<T> fieldGetterFromUserFoundedForUpdate,
                                          String fieldName, Consumer<T> fieldSetterFromUserFoundedForUpdate, Map<String, Object> changedValues) {
        if ((fieldGetterFromUserFromRequest.get() != null)
                && !fieldGetterFromUserFromRequest.get().equals(fieldGetterFromUserFoundedForUpdate.get())) {
            fieldSetterFromUserFoundedForUpdate.accept(fieldGetterFromUserFromRequest.get());
            changedValues.put(fieldName, fieldGetterFromUserFromRequest.get());
        }
    }

    private void checkAndUpdateLocalDateTimeFields(Supplier<LocalDateTime> fieldGetterFromUserFromRequest, Supplier<LocalDateTime> fieldGetterFromUserFoundedForUpdate,
                                                   String fieldName, Consumer<LocalDateTime> fieldSetterFromUserFoundedForUpdate, Map<String, Object> changedValues) {
        if ((fieldGetterFromUserFromRequest.get() != null)
                && !fieldGetterFromUserFromRequest.get().withNano(0).equals(fieldGetterFromUserFoundedForUpdate.get().withNano(0))) {
            fieldSetterFromUserFoundedForUpdate.accept(fieldGetterFromUserFromRequest.get());
            changedValues.put(fieldName, fieldGetterFromUserFromRequest.get());
        }
    }

    private String createJson(Map<String, Object> changedValues) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");

        changedValues.forEach((key, value) -> {
            jsonBuilder.append("\"")
                    .append(key)
                    .append("\": ")
                    .append("\"")
                    .append(value)
                    .append("\"")
                    .append(", ");
        });

        if (jsonBuilder.length() > 1) {
            jsonBuilder.setLength(jsonBuilder.length() - 2);
        }

        jsonBuilder.append("}");

        return jsonBuilder.toString();
    }

    private UserHistory createUserHistoryEntity(User updatedUser, String changedValues) {
        return UserHistory.builder()
                .created(LocalDateTime.now())
                .userId(updatedUser.getId())
                .reason("BY_SYSTEM")
                .userType("user")
                .changedValues(Json.of(changedValues))
                .build();
    }

}
