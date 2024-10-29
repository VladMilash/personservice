package com.mvo.personservice.util;

import com.mvo.personservice.entity.Address;
import com.mvo.personservice.entity.Country;
import com.mvo.personservice.entity.Individual;
import com.mvo.personservice.entity.User;
import dto.RegistrationRequestDTO;

import java.time.LocalDateTime;
import java.util.UUID;

public class DataUtils {

    public static RegistrationRequestDTO newRegistrationRequestDTO() {
        return new RegistrationRequestDTO(
                "John",
                "Doe",
                "Andorra",
                "123 Test St",
                "12345",
                "Test City",
                "Test State",
                "AB123456",
                "+1234567890",
                "test" + System.currentTimeMillis() + "@example.com",
                "Password123!",
                "Password123!");
    }

    public static User newUser() {
        return User.builder()
                .id(UUID.randomUUID())
                .firstName("Test")
                .lastName("User")
                .addressId(UUID.randomUUID())
                .created(LocalDateTime.now())
                .status(dto.status.UserStatus.IN_PROGRESS)
                .build();
    }

    public static Address newAddressWithoutCountryId() {
        return Address.builder()
                .id(UUID.randomUUID())
                .city("TestCity")
                .state("TestState")
                .zipCode("12345")
                .build();
    }

    public static Country newCountry() {
        return Country.builder()
                .id(1)
                .name("TestCountry")
                .build();
    }

    public static Individual newIndividualWithoutUserId() {
        return Individual.builder()
                .id(UUID.randomUUID())
                .phoneNumber("123456789")
                .email("test@example.com")
                .build();
    }
}
