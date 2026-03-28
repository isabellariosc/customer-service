package com.nttdata.customerservice.domain.service.impl;

import com.nttdata.customerservice.api.dto.CustomerCreateRequestDto;
import com.nttdata.customerservice.api.dto.CustomerResponseDto;
import com.nttdata.customerservice.api.dto.CustomerUpdateRequestDto;
import com.nttdata.customerservice.domain.mapper.CustomerMapper;
import com.nttdata.customerservice.domain.policy.CustomerValidationPolicy;
import com.nttdata.customerservice.domain.port.CustomerRecord;
import com.nttdata.customerservice.domain.port.CustomerRepositoryPort;
import com.nttdata.customerservice.domain.service.CustomerService;
import com.nttdata.customerservice.generated.api.CustomerApiDelegate;
import com.nttdata.customerservice.generated.model.CreateCustomerRequest;
import com.nttdata.customerservice.generated.model.CustomerResponse;
import com.nttdata.customerservice.generated.model.UpdateCustomerRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService, CustomerApiDelegate {

    private final CustomerValidationPolicy customerValidationPolicy;
    private final CustomerRepositoryPort customerRepositoryPort;

    @Override
    public Mono<CustomerResponseDto> create(CustomerCreateRequestDto request, String requestedBy) {
        customerValidationPolicy.validateRequestedBy(requestedBy);
        customerValidationPolicy.validateCreate(request);

        return customerRepositoryPort.findByDocumentNumber(request.getDocumentNumber())
                .flatMap(existing -> Mono.<CustomerResponseDto>error(
                        new ResponseStatusException(HttpStatus.CONFLICT, "Customer already exists")))
                .switchIfEmpty(
                        Mono.defer(() -> customerRepositoryPort.save(CustomerMapper.toNewRecord(request, requestedBy))
                                .map(CustomerMapper::toDto)))
                .cast(CustomerResponseDto.class)
                .subscribeOn(Schedulers.boundedElastic())
                .doOnError(
                        ex -> log.error("Error creating customer documentNumber={}", request.getDocumentNumber(), ex))
                .onErrorResume(DuplicateKeyException.class,
                        ex -> Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Customer already exists")));
    }

    @Override
    public Mono<CustomerResponseDto> findById(String customerId) {
        customerValidationPolicy.validateCustomerId(customerId);
        return customerRepositoryPort.findById(customerId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found")))
                .publishOn(Schedulers.parallel())
                .map(CustomerMapper::toDto)
                .doOnError(ex -> log.error("Error retrieving customerId={}", customerId, ex));
    }

    @Override
    public Flux<CustomerResponseDto> findAll(String documentNumber) {
        Flux<CustomerRecord> source = documentNumber == null || documentNumber.isBlank()
                ? customerRepositoryPort.findAll()
                : customerRepositoryPort.findByDocumentNumber(documentNumber).flux();

        return source
                .publishOn(Schedulers.parallel())
                .map(CustomerMapper::toDto)
                .doOnError(ex -> log.error("Error listing customers documentNumber={}", documentNumber, ex))
                .collectList()
                .onErrorReturn(java.util.List.of())
                .flatMapMany(Flux::fromIterable);
    }

    @Override
    public Mono<CustomerResponseDto> update(String customerId, CustomerUpdateRequestDto request, String requestedBy) {
        customerValidationPolicy.validateRequestedBy(requestedBy);
        customerValidationPolicy.validateUpdate(customerId, request);

        return customerRepositoryPort.findById(customerId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found")))
                .map(current -> CustomerMapper.merge(current, request))
                .publishOn(Schedulers.boundedElastic())
                .flatMap(customerRepositoryPort::save)
                .map(CustomerMapper::toDto)
                .doOnError(ex -> log.error("Error updating customerId={}", customerId, ex))
                .onErrorResume(DuplicateKeyException.class,
                        ex -> Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Duplicated customer data")));
    }

    @Override
    public Mono<Void> delete(String customerId, String requestedBy) {
        customerValidationPolicy.validateRequestedBy(requestedBy);
        customerValidationPolicy.validateCustomerId(customerId);

        return customerRepositoryPort.findById(customerId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found")))
                .flatMap(existing -> customerRepositoryPort.deleteById(existing.customerId()))
                .doOnError(ex -> log.error("Error deleting customerId={}", customerId, ex))
                .onErrorResume(ResponseStatusException.class, Mono::error);
    }

    @Override
    public Mono<ResponseEntity<CustomerResponse>> createCustomer(
            Mono<CreateCustomerRequest> createCustomerRequest,
            ServerWebExchange exchange) {
        return createCustomerRequest
                .map(CustomerMapper::toCreateDto)
                .flatMap(request -> create(request, authenticatedUser(exchange)))
                .map(CustomerMapper::toGenerated)
                .map(body -> ResponseEntity.status(HttpStatus.CREATED).body(body));
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteCustomer(String customerId, ServerWebExchange exchange) {
        return delete(customerId, authenticatedUser(exchange))
                .thenReturn(ResponseEntity.noContent().build());
    }

    @Override
    public Mono<ResponseEntity<CustomerResponse>> getCustomerById(String customerId, ServerWebExchange exchange) {
        return findById(customerId)
                .map(CustomerMapper::toGenerated)
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Flux<CustomerResponse>>> getCustomers(String documentNumber,
            ServerWebExchange exchange) {
        Flux<CustomerResponse> body = findAll(documentNumber).map(CustomerMapper::toGenerated);
        return Mono.just(ResponseEntity.ok(body));
    }

    @Override
    public Mono<ResponseEntity<CustomerResponse>> updateCustomer(
            String customerId,
            Mono<UpdateCustomerRequest> updateCustomerRequest,
            ServerWebExchange exchange) {
        return updateCustomerRequest
                .map(CustomerMapper::toUpdateDto)
                .flatMap(request -> update(customerId, request, authenticatedUser(exchange)))
                .map(CustomerMapper::toGenerated)
                .map(ResponseEntity::ok);
    }

    private String authenticatedUser(ServerWebExchange exchange) {
        String requestedBy = exchange.getRequest().getHeaders().getFirst("X-Auth-User");
        if (requestedBy == null || requestedBy.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing authenticated user");
        }
        return requestedBy;
    }
}