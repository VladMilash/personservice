package com.mvo.personservice.service;

import com.mvo.personservice.entity.UserHistory;
import reactor.core.publisher.Mono;

public interface UserHistoryService {
    Mono<UserHistory> save(UserHistory userHistory);
}
