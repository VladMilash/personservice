package com.mvo.personservice.service.impl;

import com.mvo.personservice.entity.Address;
import com.mvo.personservice.entity.Country;
import com.mvo.personservice.entity.Individual;
import com.mvo.personservice.entity.User;
import com.mvo.personservice.service.*;
import dto.RegistrationRequestDTO;
import dto.status.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Slf4j
@Service
public class RegistrationUsersServiceImpl implements RegistrationUsersService {
    private final UserService userService;
    private final IndividualService individualService;
    private final AddressService addressService;
    private final CountryService countryService;

    @Transactional
    @Override
    public Mono<User> registrationUser(RegistrationRequestDTO request) {
        return countryService.findByName(request.country())
                .switchIfEmpty(Mono.error(new RuntimeException("Country is not found")))
                .flatMap(country ->
                        individualService.findByEmail(request.email())
                                .flatMap(individual -> Mono.<User>error(new RuntimeException("Individual with this email already exists")))
                                .switchIfEmpty(
                                        addressService.createAddress(createdAddressEntity(request, country))
                                                .flatMap(address -> userService.createUser(createUserEntity(request, address)))
                                                .doOnError(error -> log.error("Failed to saving user"))
                                                .flatMap(user -> individualService.createIndividual(createIndividualEntity(request, user))
                                                        .doOnError(error -> log.error("Failed to saving individual"))
                                                        .thenReturn(user)
                                                        .switchIfEmpty(Mono.error(new RuntimeException("Individual was not saved"))))
                                )
                );
    }


    private User createUserEntity(RegistrationRequestDTO request, Address address) {
        return User.builder()
                .addressId(address.getId())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .filled(true)
                .verifiedAt(LocalDateTime.now())
                .archivedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .build();
    }

    private Individual createIndividualEntity(RegistrationRequestDTO request, User user) {
        return Individual.builder()
                .userId(user.getId())
                .email(request.email())
                .passportNumber(request.passportNumber())
                .phoneNumber(request.phoneNumber())
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .verifiedAt(LocalDateTime.now())
                .archivedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .build();
    }

    private Address createdAddressEntity(RegistrationRequestDTO request, Country country) {
        return Address.builder()
                .countryId(country.getId())
                .address(request.address())
                .state(request.state())
                .city(request.city())
                .zipCode(request.zipCode())
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .archived(LocalDateTime.now())
                .build();
    }
}
