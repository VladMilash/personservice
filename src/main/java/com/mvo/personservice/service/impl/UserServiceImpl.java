package com.mvo.personservice.service.impl;

import com.mvo.personservice.entity.User;
import com.mvo.personservice.exception.EntityNotFoundException;
import com.mvo.personservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements com.mvo.personservice.service.UserService {
    private final UserRepository userRepository;

    @Override
    public Mono<User> createUser(User user) {
        return userRepository.save(user)
                .doOnSuccess(savedUser -> log.info("User with id {} has been saved successfully", savedUser.getId()))
                .doOnError(error -> log.error("Failed to saving user", error));
    }

    @Override
    public Mono<User> getById(UUID id) {
        return userRepository.findById(id)
                .doOnSuccess(user -> log.info("User with id {} has been finding successfully", id))
                .doOnError(error -> log.error("Failed to finding user with id {}", id, error));
    }

    @Override
    public Flux<User> getAll() {
        return userRepository.findAll()
                .doOnNext(user -> log.info("Users has been finding successfully"))
                .doOnError(error -> log.error("Failed to finding users", error));
    }

    @Override
    public Mono<Void> deleteById(UUID id) {
        return userRepository.deleteById(id)
                .doOnSuccess(aVoid -> log.info("User with id {} has been deleted successfully", id))
                .doOnError(error -> log.error("Failed to deleted user with id {}", id, error));
    }

    @Override
    public Mono<User> updateUser(User entity) {
        return userRepository.findById(entity.getId())
                .switchIfEmpty(Mono.error(new EntityNotFoundException("User is not founded", "NOT_FOUNDED_USER")))
                .doOnError(error -> log.error("Failed to founding user for update"))
                .map(user -> setFieldsForUpdateUser(entity, user))
                .flatMap(userRepository::save)
                .doOnSuccess(user -> log.info("User with id {} has been updated successfully", user.getId()))
                .doOnError(error -> log.error("Failed to update user with id {}", entity.getId(), error));
    }

    private User setFieldsForUpdateUser(User userFromRequestForUpdate, User foundedForUpdate) {
        return foundedForUpdate.toBuilder()
                .updated(LocalDateTime.now())
                .created(userFromRequestForUpdate.getCreated())
                .filled(userFromRequestForUpdate.getFilled())
                .status(userFromRequestForUpdate.getStatus())
                .verifiedAt(userFromRequestForUpdate.getVerifiedAt())
                .firstName(userFromRequestForUpdate.getFirstName())
                .lastName(userFromRequestForUpdate.getLastName())
                .addressId(userFromRequestForUpdate.getAddressId())
                .archivedAt(userFromRequestForUpdate.getArchivedAt())
                .secretKey(userFromRequestForUpdate.getSecretKey())
                .build();
    }
}
