package com.mvo.personservice.rest;

import com.mvo.personservice.mapper.AddressMapper;
import com.mvo.personservice.mapper.CountryMapper;
import com.mvo.personservice.mapper.IndividualMapper;
import com.mvo.personservice.mapper.UserMapper;
import com.mvo.personservice.service.UserService;
import dto.AddressDTO;
import dto.CountryDTO;
import dto.IndividualDTO;
import dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/api/users/")
public class UserRestControllerV1 {
    private final UserService userService;
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;
    private final CountryMapper countryMapper;
    private final IndividualMapper individualMapper;

    @GetMapping
    public Flux<UserDTO> getAll() {
        return userService.getAll()
                .map(userMapper::map);
    }

    @GetMapping("{userId}")
    public Mono<UserDTO> getById(@PathVariable("userId") UUID id) {
        return userService.getById(id)
                .map(userMapper::map);
    }

    @GetMapping("address/{userId}")
    public Mono<AddressDTO> getAddressByUserId(@PathVariable("userId") UUID id) {
        return userService.getUserAddressByUserId(id)
                .map(addressMapper::map);
    }

    @GetMapping("country/{userId}")
    public Mono<CountryDTO> getCountryByUserId(@PathVariable("userId") UUID id) {
        return userService.getUserCountryByUserId(id)
                .map(countryMapper::map);
    }

    @GetMapping("individuals/{userId}")
    public Flux<IndividualDTO> getIndividualsByUserId(@PathVariable("userId") UUID id) {
        return userService.getIndividualsByUserId(id)
                .map(individualMapper::map);
    }

}
