package com.mvo.personservice.service.impl;

import com.mvo.personservice.entity.Address;
import com.mvo.personservice.entity.Country;
import com.mvo.personservice.entity.Individual;
import com.mvo.personservice.entity.User;
import com.mvo.personservice.repository.UserRepository;
import com.mvo.personservice.service.AddressService;
import com.mvo.personservice.service.CountryService;
import com.mvo.personservice.service.IndividualService;
import com.mvo.personservice.util.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AddressService addressService;

    @Mock
    private CountryService countryService;

    @Mock
    private IndividualService individualService;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private Address testAddress;
    private Country testCountry;
    private Individual testIndividual;

    @BeforeEach
    void setUp() {
        testUser = TestDataFactory.newUser();

        testAddress = TestDataFactory.newAddressWithoutCountryId()
                .toBuilder()
                .id(testUser.getAddressId())
                .build();

        testCountry = TestDataFactory.newCountry();

        testIndividual = TestDataFactory.newIndividualWithoutUserId()
                .toBuilder()
                .userId(testUser.getId())
                .build();
    }

    @Test
    void createUser() {
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(testUser));

        StepVerifier.create(userService.createUser(testUser))
                .expectNextMatches(user -> user.getId().equals(testUser.getId()))
                .verifyComplete();
    }

    @Test
    void getById_existingUser() {
        when(userRepository.findById(testUser.getId())).thenReturn(Mono.just(testUser));

        StepVerifier.create(userService.getById(testUser.getId()))
                .expectNextMatches(user -> user.getId().equals(testUser.getId()))
                .verifyComplete();
    }


    @Test
    void getUserAddressByUserId() {
        when(userRepository.findById(testUser.getId())).thenReturn(Mono.just(testUser));
        when(addressService.getById(testUser.getAddressId())).thenReturn(Mono.just(testAddress));

        StepVerifier.create(userService.getUserAddressByUserId(testUser.getId()))
                .expectNextMatches(address -> address.getId().equals(testAddress.getId()))
                .verifyComplete();
    }

    @Test
    void getUserCountryByUserId() {
        when(userRepository.findById(testUser.getId())).thenReturn(Mono.just(testUser));
        when(addressService.getById(testUser.getAddressId())).thenReturn(Mono.just(testAddress));
        when(countryService.findById(testAddress.getCountryId())).thenReturn(Mono.just(testCountry));

        StepVerifier.create(userService.getUserCountryByUserId(testUser.getId()))
                .expectNextMatches(country -> country.getId().equals(testCountry.getId()))
                .verifyComplete();
    }

    @Test
    void getIndividualsByUserId() {
        when(userRepository.findById(testUser.getId())).thenReturn(Mono.just(testUser));
        when(individualService.findByUserId(testUser.getId())).thenReturn(Mono.just(testIndividual));

        StepVerifier.create(userService.getIndividualsByUserId(testUser.getId()))
                .expectNextMatches(individual -> individual.getUserId().equals(testUser.getId()))
                .verifyComplete();
    }

    @Test
    void deleteUserById() {
        when(userRepository.deleteById(testUser.getId())).thenReturn(Mono.empty());

        StepVerifier.create(userService.deleteById(testUser.getId()))
                .verifyComplete();
    }
}
