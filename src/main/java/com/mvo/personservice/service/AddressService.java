package com.mvo.personservice.service;

import com.mvo.personservice.entity.Address;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface AddressService {
    Mono<Address> createAddress(Address address);

    Mono<Address> getById(UUID id);
}
