package com.mvo.personservice.service.impl;

import com.mvo.personservice.entity.Address;
import com.mvo.personservice.repository.AddressRepository;
import com.mvo.personservice.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
@Service
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;

    @Override
    public Mono<Address> createAddress(Address address) {
        return addressRepository.save(address)
                .doOnSuccess(savedAddress -> log.info("Address with id {} has been saved successfully", savedAddress.getId()))
                .doOnError(error -> log.error("Failed to saving address", error));
    }
}
