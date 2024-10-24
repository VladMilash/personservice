package com.mvo.personservice.service;

import com.mvo.personservice.entity.Address;
import com.mvo.personservice.entity.UserHistory;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface AddressService {
    Mono<Address> createAddress(Address address);

    Mono<Address> getById(UUID id);

    Mono<UserHistory> updateAddress(UUID userId, Address entity);

    Mono<Void> deleteById(UUID id);
}
