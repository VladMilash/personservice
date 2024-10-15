package com.mvo.personservice.repository;

import com.mvo.personservice.entity.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import java.util.UUID;

public interface UserRepository extends R2dbcRepository<User, UUID> {
}
