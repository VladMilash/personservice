package com.mvo.personservice.service;

import com.mvo.personservice.entity.Address;
import com.mvo.personservice.entity.Country;
import com.mvo.personservice.entity.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserService {
    Mono<User> createUser(User user);

    Mono<User> getById(UUID id);

    Flux<User> getAll();

    Mono<User> updateUser(User entity);

    Mono<Address> getUserAddressByUserId(UUID id);

    Mono<Country> getUserCountryByUserId(UUID id);
}