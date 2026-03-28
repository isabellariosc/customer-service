package com.nttdata.customerservice.domain.port;

import com.nttdata.customerservice.api.dto.CustomerCreateRequestDto;
import com.nttdata.customerservice.api.dto.CustomerResponseDto;
import com.nttdata.customerservice.api.dto.CustomerUpdateRequestDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerUseCase {

    Mono<CustomerResponseDto> create(CustomerCreateRequestDto request, String requestedBy);

    Mono<CustomerResponseDto> findById(String customerId);

    Flux<CustomerResponseDto> findAll(String documentNumber);

    Mono<CustomerResponseDto> update(String customerId, CustomerUpdateRequestDto request, String requestedBy);

    Mono<Void> delete(String customerId, String requestedBy);
}