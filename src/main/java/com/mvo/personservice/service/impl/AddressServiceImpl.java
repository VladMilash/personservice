package com.mvo.personservice.service.impl;

import com.mvo.personservice.entity.Address;
import com.mvo.personservice.entity.UserHistory;
import com.mvo.personservice.exception.EntityNotFoundException;
import com.mvo.personservice.repository.AddressRepository;
import com.mvo.personservice.service.AddressService;
import com.mvo.personservice.service.UserService;
import com.mvo.personservice.service.impl.util.UpdateEntityHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final UserService userService;
    private final UpdateEntityHelper updateEntityHelper;

    public AddressServiceImpl(
            AddressRepository addressRepository,
            @Lazy UserService userService,
            UpdateEntityHelper updateEntityHelper
    ) {
        this.addressRepository = addressRepository;
        this.userService = userService;
        this.updateEntityHelper = updateEntityHelper;
    }

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
    public Mono<UserHistory> updateAddress(UUID userId, Address entity) {
        return userService.getById(userId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Not found user with this id", "NOT_FOUNDED_USER")))
                .flatMap(user -> getById(user.getAddressId())
                        .switchIfEmpty(Mono.error(new EntityNotFoundException("Not found address with this id", "NOT_FOUNDED_ADDRESS")))
                        .flatMap(foundedAddress -> {
                            Map<String, Object> changedValues = new HashMap<>();
                            Address updatedAddress = updateAddressFields(entity, foundedAddress, changedValues);
                            return createAddress(updatedAddress)
                                    .then(Mono.just(updateEntityHelper.createUserHistoryEntity(user, updateEntityHelper.createJson(changedValues))));
                        }))
                .doOnSuccess(userHistory -> log.info("Address for user with id {} has been updated successfully", userId))
                .doOnError(error -> log.error("Failed to update address for user with id {}", userId, error));
    }

    private Address updateAddressFields(Address addressFromRequestForUpdate, Address addressFoundedForUpdate,
                                        Map<String, Object> changedValues) {
        updateEntityHelper.checkAndUpdateFields(addressFromRequestForUpdate::getAddress, addressFoundedForUpdate::getAddress,
                "address", addressFoundedForUpdate::setAddress, changedValues);

        updateEntityHelper.checkAndUpdateFields(addressFromRequestForUpdate::getZipCode, addressFoundedForUpdate::getZipCode,
                "zip_code", addressFoundedForUpdate::setZipCode, changedValues);

        updateEntityHelper.checkAndUpdateLocalDateTimeFields(addressFromRequestForUpdate::getArchived, addressFoundedForUpdate::getArchived,
                "archived", addressFoundedForUpdate::setArchived, changedValues);

        updateEntityHelper.checkAndUpdateFields(addressFromRequestForUpdate::getCity, addressFoundedForUpdate::getCity,
                "city", addressFoundedForUpdate::setCity, changedValues);

        updateEntityHelper.checkAndUpdateFields(addressFromRequestForUpdate::getState, addressFoundedForUpdate::getState,
                "state", addressFoundedForUpdate::setState, changedValues);

        return addressFoundedForUpdate;
    }
}
