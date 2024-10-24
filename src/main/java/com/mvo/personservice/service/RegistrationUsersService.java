package com.mvo.personservice.service;

import com.mvo.personservice.entity.User;
import dto.RegistrationRequestDTO;
import reactor.core.publisher.Mono;

public interface RegistrationUsersService {
    Mono<User> registrationUser(RegistrationRequestDTO request);

    Mono<Void> rollBeckRegistration(RegistrationRequestDTO requestDTO);
}
