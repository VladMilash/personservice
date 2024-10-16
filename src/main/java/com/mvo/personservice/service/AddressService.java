package com.mvo.personservice.service;

import com.mvo.personservice.entity.Address;
import reactor.core.publisher.Mono;

public interface AddressService {
    Mono<Address> createAddress(Address address);
}
