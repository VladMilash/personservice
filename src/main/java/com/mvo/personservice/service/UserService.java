package com.mvo.personservice.service;

import com.mvo.personservice.entity.User;
import dto.RegistrationRequestDTO;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<User> createUser(User user);
}