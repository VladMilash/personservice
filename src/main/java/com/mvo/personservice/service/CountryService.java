package com.mvo.personservice.service;

import com.mvo.personservice.entity.Country;
import reactor.core.publisher.Mono;

public interface CountryService {
    Mono<Country> findByName(String name);

    Mono<Country> findById(Integer id);
}
