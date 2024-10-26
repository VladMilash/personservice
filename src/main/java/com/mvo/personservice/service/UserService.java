package com.mvo.personservice.service;

import com.mvo.personservice.entity.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserService {
    Mono<User> createUser(User user);

    Mono<User> getById(UUID id);

    Flux<User> getAll();

    Mono<UserHistory> updateUser(User entity);

    Mono<Address> getUserAddressByUserId(UUID id);

    Mono<Country> getUserCountryByUserId(UUID id);

    Mono<Individual> getIndividualsByUserId(UUID id);

    Mono<Void> deleteById(UUID id);
}