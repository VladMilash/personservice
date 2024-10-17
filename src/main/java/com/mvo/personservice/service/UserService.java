package com.mvo.personservice.service;

import com.mvo.personservice.entity.User;
import dto.RegistrationRequestDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserService {
    Mono<User> createUser(User user);

    Mono<User> getById(UUID id);

    Flux<User> getAll();

    Mono<Void> deleteById(UUID id);

    Mono<User> updateUser(User entity);
}