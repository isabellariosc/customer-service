package com.nttdata.customerservice.domain.port;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerRepositoryPort {

    Mono<CustomerRecord> save(CustomerRecord customerRecord);

    Mono<CustomerRecord> findById(String customerId);

    Mono<CustomerRecord> findByDocumentNumber(String documentNumber);

    Flux<CustomerRecord> findAll();

    Mono<Void> deleteById(String customerId);
}