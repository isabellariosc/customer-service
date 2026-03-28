package com.nttdata.customerservice.infrastructure.mongodb;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CustomerMongoRepository extends ReactiveMongoRepository<CustomerDocument, String> {

    Mono<CustomerDocument> findByDocumentNumber(String documentNumber);
}