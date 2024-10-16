package com.mvo.personservice.service.impl;

import com.mvo.personservice.entity.User;
import com.mvo.personservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements com.mvo.personservice.service.UserService {
    private final UserRepository userRepository;
    @Override
    public Mono<User> createUser(User user) {
        return userRepository.save(user)
                .doOnSuccess(savedUser -> log.info("User with id {} has been saved successfully", savedUser.getId()))
                .doOnError(error -> log.error("Failed to saving user", error));
    }
}
