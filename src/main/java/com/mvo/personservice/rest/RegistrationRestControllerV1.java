package com.mvo.personservice.rest;

import com.mvo.personservice.entity.User;
import com.mvo.personservice.mapper.UserMapper;
import com.mvo.personservice.service.RegistrationUsersService;
import dto.RegistrationRequestDTO;
import dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

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
}
