package com.mvo.personservice.repository;

import com.mvo.personservice.entity.UserHistory;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserHistoryRepository extends R2dbcRepository<UserHistory, UUID> {
}
