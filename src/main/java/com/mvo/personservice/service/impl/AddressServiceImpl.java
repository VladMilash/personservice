package com.mvo.personservice.service.impl;

import com.mvo.personservice.entity.Address;
import com.mvo.personservice.entity.UserHistory;
import com.mvo.personservice.repository.AddressRepository;
import com.mvo.personservice.service.AddressService;
import com.mvo.personservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final UserService userService;

    @Override
    public Mono<Address> createAddress(Address address) {
        return addressRepository.save(address)
                .doOnSuccess(savedAddress -> log.info("Address with id {} has been saved successfully", savedAddress.getId()))
                .doOnError(error -> log.error("Failed to saving address", error));
    }

    @Override
    public Mono<Address> getById(UUID id) {
        return addressRepository.findById(id)
                .doOnSuccess(fouindedAddress -> log.info("Address with id {} has been founded successfully", id))
                .doOnError(error -> log.error("Failed to founding address", error));
    }

    @Override
    public Mono<UserHistory> updateAddress(UUID userId) {
        return null;
    }
}
