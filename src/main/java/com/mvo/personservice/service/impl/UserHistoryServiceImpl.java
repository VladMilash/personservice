package com.mvo.personservice.service.impl;

import com.mvo.personservice.entity.UserHistory;
import com.mvo.personservice.repository.UserHistoryRepository;
import com.mvo.personservice.service.UserHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserHistoryServiceImpl implements UserHistoryService {
    private final UserHistoryRepository userHistoryRepository;

    @Override
    public Mono<UserHistory> save(UserHistory userHistory) {
        return userHistoryRepository.save(userHistory)
                .doOnSuccess(savedUserHistory -> log.info("userHistory with id {} has been saved successfully", savedUserHistory.getId()))
                .doOnError(error -> log.error("Failed to saving userHistory", error));
    }
}
