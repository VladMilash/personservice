package com.mvo.personservice.repository;

import com.mvo.personservice.entity.Address;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import java.util.UUID;

public interface AddressRepository extends R2dbcRepository<Address, UUID> {
}
