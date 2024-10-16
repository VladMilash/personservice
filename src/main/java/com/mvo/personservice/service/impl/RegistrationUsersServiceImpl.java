package com.mvo.personservice.service.impl;

import com.mvo.personservice.entity.Address;
import com.mvo.personservice.entity.Individual;
import com.mvo.personservice.entity.User;
import com.mvo.personservice.service.AddressService;
import com.mvo.personservice.service.IndividualService;
import com.mvo.personservice.service.RegistrationUsersService;
import com.mvo.personservice.service.UserService;
import dto.RegistrationRequestDTO;
import dto.status.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Slf4j
@Service
public class RegistrationUsersServiceImpl implements RegistrationUsersService {
    private final UserService userService;
    private final IndividualService individualService;
    private final AddressService addressService;

    @Override
    public Mono<User> registrationUser(RegistrationRequestDTO request) {
        return individualService.findByEmail(request.email())
                .switchIfEmpty(userService.createUser())
    }

    private User createUserEntity(RegistrationRequestDTO request) {
        return User.builder()
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

    private Individual createIndividualEntity(RegistrationRequestDTO request) {
        return Individual.builder()
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

    private Address createdAddressEntity(RegistrationRequestDTO request) {
        return Address.builder()
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
