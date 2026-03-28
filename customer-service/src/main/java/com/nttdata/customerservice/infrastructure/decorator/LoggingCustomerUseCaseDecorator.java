package com.nttdata.customerservice.infrastructure.decorator;

import com.nttdata.customerservice.api.dto.CustomerCreateRequestDto;
import com.nttdata.customerservice.api.dto.CustomerResponseDto;
import com.nttdata.customerservice.api.dto.CustomerUpdateRequestDto;
import com.nttdata.customerservice.domain.port.CustomerUseCase;
import com.nttdata.customerservice.domain.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Primary
@Component
@RequiredArgsConstructor
public class LoggingCustomerUseCaseDecorator implements CustomerUseCase {

    private final CustomerService delegate;

    @Override
    public Mono<CustomerResponseDto> create(CustomerCreateRequestDto request, String requestedBy) {
        log.info("Create customer requested by user={} documentNumber={}", requestedBy, request.getDocumentNumber());
        return delegate.create(request, requestedBy);
    }

    @Override
    public Mono<CustomerResponseDto> findById(String customerId) {
        log.info("Find customer requested customerId={}", customerId);
        return delegate.findById(customerId);
    }

    @Override
    public Flux<CustomerResponseDto> findAll(String documentNumber) {
        log.info("List customers requested documentNumber={}", documentNumber);
        return delegate.findAll(documentNumber);
    }

    @Override
    public Mono<CustomerResponseDto> update(String customerId, CustomerUpdateRequestDto request, String requestedBy) {
        log.info("Update customer requested by user={} customerId={}", requestedBy, customerId);
        return delegate.update(customerId, request, requestedBy);
    }

    @Override
    public Mono<Void> delete(String customerId, String requestedBy) {
        log.info("Delete customer requested by user={} customerId={}", requestedBy, customerId);
        return delegate.delete(customerId, requestedBy);
    }
}