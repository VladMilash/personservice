package com.mvo.personservice.service.impl;

import com.mvo.personservice.entity.User;
import com.mvo.personservice.service.AddressService;
import com.mvo.personservice.service.IndividualService;
import com.mvo.personservice.service.RegistrationUsersService;
import com.mvo.personservice.service.UserService;
import dto.RegistrationRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
@Service
public class RegistrationUsersServiceImpl implements RegistrationUsersService {
    private final UserService userService;
    private final IndividualService individualService;
    private final AddressService addressService;

    @Override
    public Mono<User> registrationUser(RegistrationRequestDTO request) {
        return null;
    }
}
