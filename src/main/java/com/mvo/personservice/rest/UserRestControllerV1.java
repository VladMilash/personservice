package com.mvo.personservice.rest;

import com.mvo.personservice.mapper.*;
import com.mvo.personservice.service.AddressService;
import com.mvo.personservice.service.IndividualService;
import com.mvo.personservice.service.UserService;
import dto.*;
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
    private final UserHistoryMapper userHistoryMapper;
    private final AddressService addressService;
    private final IndividualService individualService;

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
    public Mono<IndividualDTO> getIndividualsByUserId(@PathVariable("userId") UUID id) {
        return userService.getIndividualsByUserId(id)
                .map(individualMapper::map);
    }

    @PutMapping()
    public Mono<UserHistoryDTO> updateUser
            (@RequestBody UserDTO userDTO) {
        return userService.updateUser(userMapper.map(userDTO))
                .map(userHistoryMapper::map);
    }

    @PutMapping("address/{userId}")
    public Mono<UserHistoryDTO> updateAddress
            (@PathVariable("userId") UUID userId, @RequestBody AddressDTO addressDTO) {
        return addressService.updateAddress(userId, addressMapper.map(addressDTO))
                .map(userHistoryMapper::map);
    }

    @PutMapping("individuals")
    public Mono<UserHistoryDTO> updateIndividual(@RequestBody IndividualDTO individualDTO) {
        return individualService.updateIndividual(individualMapper.map(individualDTO))
                .map(userHistoryMapper::map);
    }

    @GetMapping("email/{email}")
    public Mono<UserDTO> getUserByEmail(@PathVariable String email) {
        return userService.getByUserEmail(email)
                .map(userMapper::map);
    }
}
