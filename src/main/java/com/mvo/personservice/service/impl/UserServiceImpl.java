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

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
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
        Map<String, Object> changedValues = new HashMap<>();
        return userRepository.findById(entity.getId())
                .switchIfEmpty(Mono.error(new EntityNotFoundException("User is not founded", "NOT_FOUNDED_USER")))
                .doOnError(error -> log.error("Failed to finding user for update"))
                .map(user-> processingOfUpdatingUser(entity,user,changedValues))
                .flatMap(userHistoryService::save)
                .doOnSuccess(userHistory -> log.info("User with id {} has been updated successfully", entity.getId()))
                .doOnError(error -> log.error("Failed to update user with id {}", entity.getId(), error));
    }

    private UserHistory processingOfUpdatingUser(User userFromRequestForUpdate, User userFoundedForUpdate, Map<String, Object> changedValues) {
        Map<String, Object> map = changedValues;
        User updatedUser = checkAndSetFieldsForUprate(userFromRequestForUpdate, userFoundedForUpdate, map);
        String json = createJson(map);
        return createUserHistoryEntity(updatedUser,json);
    }

    private User checkAndSetFieldsForUprate(User userFromRequestForUpdate, User userFoundedForUpdate, Map<String, Object> changedValues) {
        if ((userFromRequestForUpdate.getSecretKey() != null) && (userFromRequestForUpdate.getSecretKey().equals(userFoundedForUpdate.getSecretKey()))) {
            userFoundedForUpdate.setSecretKey(userFromRequestForUpdate.getSecretKey());
            changedValues.put("SecretKey", userFromRequestForUpdate.getSecretKey());
        }

        if ((userFromRequestForUpdate.getFirstName() != null) && (userFromRequestForUpdate.getFirstName().equals(userFoundedForUpdate.getFirstName()))) {
            userFoundedForUpdate.setFirstName(userFromRequestForUpdate.getFirstName());
            changedValues.put("First_name", userFromRequestForUpdate.getFirstName());
        }

        if ((userFromRequestForUpdate.getLastName() != null) && (userFromRequestForUpdate.getLastName().equals(userFoundedForUpdate.getLastName()))) {
            userFoundedForUpdate.setLastName(userFromRequestForUpdate.getLastName());
            changedValues.put("Last_name", userFromRequestForUpdate.getLastName());
        }

        if ((userFromRequestForUpdate.getVerifiedAt() != null) && (userFromRequestForUpdate.getVerifiedAt().equals(userFoundedForUpdate.getVerifiedAt()))) {
            userFoundedForUpdate.setVerifiedAt(userFromRequestForUpdate.getVerifiedAt());
            changedValues.put("Verified_at", userFromRequestForUpdate.getVerifiedAt());
        }

        if ((userFromRequestForUpdate.getArchivedAt() != null) && (userFromRequestForUpdate.getArchivedAt().equals(userFoundedForUpdate.getArchivedAt()))) {
            userFoundedForUpdate.setArchivedAt(userFromRequestForUpdate.getArchivedAt());
            changedValues.put("Archived_at", userFromRequestForUpdate.getArchivedAt());
        }

        if ((userFromRequestForUpdate.getStatus() != null) && (userFromRequestForUpdate.getStatus().equals(userFoundedForUpdate.getStatus()))) {
            userFoundedForUpdate.setStatus(userFromRequestForUpdate.getStatus());
            changedValues.put("Status", userFromRequestForUpdate.getStatus());
        }

        if ((userFromRequestForUpdate.getFilled() != null) && (userFromRequestForUpdate.getFilled().equals(userFoundedForUpdate.getFilled()))) {
            userFoundedForUpdate.setFilled(userFromRequestForUpdate.getFilled());
            changedValues.put("Filled", userFromRequestForUpdate.getFilled());
        }
        return userFoundedForUpdate;
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

        System.out.println(jsonBuilder.toString());

        return jsonBuilder.toString();
    }

    private UserHistory createUserHistoryEntity(User updatedUser, String changedValues) {

        System.out.println(Json.of(changedValues));
        return UserHistory.builder()
                .created(LocalDateTime.now())
                .userId(updatedUser.getId())
                .reason("BY_SYSTEM")
                .userType("user")
                .changedValues(Json.of(changedValues))
                .build();
    }

}
