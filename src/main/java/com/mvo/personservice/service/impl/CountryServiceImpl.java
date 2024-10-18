package com.mvo.personservice.service.impl;

import com.mvo.personservice.entity.Country;
import com.mvo.personservice.repository.CountryRepository;
import com.mvo.personservice.service.CountryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
@Service
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;

    @Override
    public Mono<Country> findByName(String name) {
        return countryRepository.findByName(name)
                .doOnSuccess(country -> log.info("Operation for founding country has successfully done"))
                .doOnError(error -> log.error("Failed to founding country", error));
    }

    @Override
    public Mono<Country> findById(Integer id) {
        return countryRepository.findById(id)
                .doOnSuccess(country -> log.info("Country with id {} was found successfully", id))
                .doOnError(error -> log.error("Failed to founding country", error));
    }
}
