package com.mvo.personservice.it;

import com.mvo.personservice.config.PostgreTestcontainerConfig;
import dto.*;
import dto.status.Status;
import dto.status.UserStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Import(PostgreTestcontainerConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class ItRegistrationRestControllerV1AndUserRestControllerV1Test {

    @Autowired
    private WebTestClient webTestClient;

    private RegistrationRequestDTO registrationRequestDTO;
    private UserDTO testUserDTO;
    private IndividualDTO testIndividualDTO;


    @BeforeEach
    void setup() {
        registrationRequestDTO = new RegistrationRequestDTO(
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
                "Password123!"
        );

    }

    @Test
    void testUserRegistration_Success() {
        testUserDTO = webTestClient.post()
                .uri("/v1/api/registration/")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(registrationRequestDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDTO.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(testUserDTO);
        assertNotNull(testUserDTO.id());
    }

    @Test
    void testUserRegistrationRollBack_Success() {
        testUserRegistration_Success();

        webTestClient.delete()
                .uri("/v1/api/registration/rollback/" + testUserDTO.id())
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testGetAll_Success() {
        List<UserDTO> users = webTestClient.get()
                .uri("/v1/api/users/")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserDTO.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(users);
    }

    @Test
    void testGetById_Success() {
        testUserRegistration_Success();

        UserDTO foundedUser = webTestClient.get()
                .uri("/v1/api/users/" + testUserDTO.id())
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDTO.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(foundedUser);
        Assertions.assertEquals(foundedUser.id(), testUserDTO.id());
    }

    @Test
    void getAddressByUserId() {
        testUserRegistration_Success();

        AddressDTO foundedAddress = webTestClient.get()
                .uri("/v1/api/users/address/" + testUserDTO.id())
                .exchange()
                .expectStatus().isOk()
                .expectBody(AddressDTO.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(foundedAddress);
        Assertions.assertEquals(foundedAddress.id(), testUserDTO.addressId());
    }

    @Test
    void getCountryByUserId() {
        testUserRegistration_Success();

        CountryDTO foundedCountry = webTestClient.get()
                .uri("/v1/api/users/country/" + testUserDTO.id())
                .exchange()
                .expectStatus().isOk()
                .expectBody(CountryDTO.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(foundedCountry);
    }

    @Test
    void getIndividualsByUserId() {
        testUserRegistration_Success();

        testIndividualDTO = webTestClient.get()
                .uri("/v1/api/users/individuals/" + testUserDTO.id())
                .exchange()
                .expectStatus().isOk()
                .expectBody(IndividualDTO.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(testIndividualDTO);
        Assertions.assertEquals(testIndividualDTO.userId(), testUserDTO.id());

    }

    @Test
    void updateUser_Success() {
        testUserRegistration_Success();

        UserDTO updateUserDTO = new UserDTO(
                testUserDTO.id(),
                "newSecretKey",
                LocalDateTime.now(),
                LocalDateTime.now(),
                "UpdatedFirst",
                "UpdatedLast",
                LocalDateTime.now(),
                null,
                UserStatus.VERIFIED,
                true,
                testUserDTO.addressId()
        );

        UserHistoryDTO gotUserHistoryDTO = webTestClient.put()
                .uri("/v1/api/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateUserDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserHistoryDTO.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(gotUserHistoryDTO);

    }

    @Test
    void testUpdateUserAddress_Success() {
        testUserRegistration_Success();

        AddressDTO addressDTO = new AddressDTO(
                UUID.randomUUID(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                1,
                "456 New Street",
                "54321",
                null,
                "New City",
                "New State"
        );

        UserHistoryDTO gotUserHistoryDTO = webTestClient.put()
                .uri("/v1/api/users/address/" + testUserDTO.id())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(addressDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserHistoryDTO.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(gotUserHistoryDTO);
    }

    @Test
    void testUpdateUserIndividuals_Success() {
        getIndividualsByUserId();

        IndividualDTO individualDTO = new IndividualDTO(
                testIndividualDTO.id(),
                testUserDTO.id(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                "CD789012",
                "+9876543210",
                "updated1@example.com",
                LocalDateTime.now(),
                null,
                Status.ACTIVE
        );

        UserHistoryDTO gotUserHistoryDTO = webTestClient.put()
                .uri("/v1/api/users/individuals")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(individualDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserHistoryDTO.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(gotUserHistoryDTO);

    }

    @Test
    void getUserByEmail_Success() {
        getIndividualsByUserId();

        UserDTO foundedUser = webTestClient.get()
                .uri("/v1/api/users/email/" + testIndividualDTO.email())
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDTO.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(foundedUser);
        Assertions.assertEquals(foundedUser.id(), testUserDTO.id());

    }

}
