package com.mvo.personservice.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import java.util.UUID;

public interface UserHistory extends R2dbcRepository<UserHistory, UUID> {
}
