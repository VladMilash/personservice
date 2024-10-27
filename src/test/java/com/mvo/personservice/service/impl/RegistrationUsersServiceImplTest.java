package com.mvo.personservice.service.impl;

import com.mvo.personservice.entity.Address;
import com.mvo.personservice.entity.Country;
import com.mvo.personservice.entity.Individual;
import com.mvo.personservice.entity.User;
import dto.RegistrationRequestDTO;
import dto.status.Status;
import dto.status.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RegistrationUsersServiceImplTest {

    @Mock
    private IndividualServiceImpl individualService;

    @Mock
    private AddressServiceImpl addressService;

    @Mock
    private CountryServiceImpl countryService;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private RegistrationUsersServiceImpl registrationUsersService;

    private RegistrationRequestDTO requestDTO;

    private User user;

    private Country country;

    private Address address;

    private Individual individual;

    @BeforeEach
    void setUp() {
        setUpTestData();

    }

    private void setUpTestData() {
        requestDTO = new RegistrationRequestDTO(
                "firstName",
                "lastName",
                "country",
                "address",
                "zipCode",
                "city",
                "state",
                "phoneNumber",
                "email",
                "password",
                "confirmPassword",
                "confirmPassword"
        );

        user = User.builder()
                .id(UUID.randomUUID())
                .secretKey("test-secret-key")
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .firstName("TestFirstName")
                .lastName("TestLastName")
                .verifiedAt(null)
                .archivedAt(null)
                .filled(true)
                .status(UserStatus.IN_PROGRESS)
                .addressId(UUID.randomUUID())
                .build();

        country = Country.builder()
                .id(1)
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .name("TestCountryName")
                .alpha2("TC")
                .status(Status.ACTIVE)
                .build();

        address = Address.builder()
                .id(UUID.randomUUID())
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .countryId(country.getId())
                .address(requestDTO.address())
                .state(requestDTO.state())
                .city(requestDTO.city())
                .zipCode(requestDTO.zipCode())
                .build();

        individual = Individual.builder()
                .id(UUID.randomUUID())
                .userId(user.getId())
                .email(requestDTO.email())
                .passportNumber(requestDTO.passportNumber())
                .phoneNumber(requestDTO.phoneNumber())
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .status(Status.ACTIVE)
                .build();

    }


    @Test
    void registrationUser() {
        when(countryService.findByName(any(String.class))).thenReturn(Mono.just(country));
        when(individualService.findByEmail(any(String.class))).thenReturn(Mono.empty());
        when(addressService.createAddress(any(Address.class))).thenReturn(Mono.just(address));
        when(userService.createUser(any(User.class))).thenReturn(Mono.just(user));
        when(individualService.createIndividual(any(Individual.class))).thenReturn(Mono.just(individual));

        StepVerifier.create(registrationUsersService.registrationUser(requestDTO))
                .expectNextMatches(result -> {
                    assertNotNull(result);
                    assertEquals("TestFirstName", result.getFirstName());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void rollBeckRegistration() {
        when(userService.getById(any(UUID.class))).thenReturn(Mono.just(user));
        when(addressService.getById(any(UUID.class))).thenReturn(Mono.just(address));
        when(individualService.findByUserId(any(UUID.class))).thenReturn(Mono.just(individual));
        when(individualService.deleteById(any(UUID.class))).thenReturn(Mono.empty());
        when(userService.deleteById(any(UUID.class))).thenReturn(Mono.empty());
        when(addressService.deleteById(any(UUID.class))).thenReturn(Mono.empty());

        StepVerifier.create(registrationUsersService.rollBeckRegistration(user.getId()))
                .verifyComplete();

        verify(individualService).deleteById(individual.getId());
        verify(userService).deleteById(user.getId());
        verify(addressService).deleteById(address.getId());
    }
}