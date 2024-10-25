package com.mvo.personservice.service.impl;

import com.mvo.personservice.entity.Address;
import com.mvo.personservice.entity.Country;
import com.mvo.personservice.entity.Individual;
import com.mvo.personservice.entity.User;
import com.mvo.personservice.exception.ApiException;
import com.mvo.personservice.exception.EntityAlreadyExistException;
import com.mvo.personservice.exception.EntityNotFoundException;
import com.mvo.personservice.service.*;
import dto.RegistrationRequestDTO;
import dto.status.Status;
import dto.status.UserStatus;
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
        log.info("Request:{}", request.toString());
        return countryService.findByName(request.country())
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Country is not found", "COUNTRY_IS_NOT_FOUND")))
                .flatMap(country ->
                        individualService.findByEmail(request.email())
                                .flatMap(individual -> Mono.<User>error(new EntityAlreadyExistException("Individual with this email already exists", "INDIVIDUAL_ALREADY_EXISTS")))
                                .switchIfEmpty(
                                        addressService.createAddress(createdAddressEntity(request, country))
                                                .flatMap(address -> userService.createUser(createUserEntity(request, address)))
                                                .doOnError(error -> log.error("Failed to saving user"))
                                                .flatMap(user -> individualService.createIndividual(createIndividualEntity(request, user))
                                                        .doOnError(error -> log.error("Failed to saving individual", error))
                                                        .thenReturn(user)
                                                        .switchIfEmpty(Mono.error(new ApiException("Individual was not saved", "FAILED_SAVING_INDIVIDUAL"))))
                                )
                );
    }

    @Override
    public Mono<Void> rollBeckRegistration(RegistrationRequestDTO request) {
        return individualService.findByPassportNumber(request.passportNumber())
                .flatMap(individual -> {
                    return userService.getById(individual.getUserId())
                            .flatMap(user -> {
                                return individualService.deleteById(individual.getId())
                                        .then(userService.deleteById(user.getId()))
                                        .then(addressService.deleteById(user.getAddressId()));
                            });

                })
                .doOnError(error -> log.error("RollBeck registration failed"))
                .doOnSuccess(aVoid -> log.info("Operation for rollBeck registration has finished successfully"));
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
                .status(UserStatus.IN_PROGRESS)
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
