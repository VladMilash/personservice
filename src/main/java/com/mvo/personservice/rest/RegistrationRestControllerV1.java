package com.mvo.personservice.rest;

import com.mvo.personservice.mapper.UserMapper;
import com.mvo.personservice.service.RegistrationUsersService;
import dto.RegistrationRequestDTO;
import dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/api/registration/")
public class RegistrationRestControllerV1 {
    private final RegistrationUsersService registrationUsersService;
    private final UserMapper userMapper;

    @PostMapping
    public Mono<UserDTO> registration
            (@RequestBody RegistrationRequestDTO request) {
        return registrationUsersService.registrationUser(request)
                .map(userMapper::map);
    }

    @DeleteMapping("rollback/{userId}")
    public Mono<Void> doRollBeckRegistration
            (@PathVariable UUID userId) {
        return registrationUsersService.rollBeckRegistration(userId);
    }
}
