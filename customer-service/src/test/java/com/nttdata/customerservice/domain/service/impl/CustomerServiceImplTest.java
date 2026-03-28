package com.nttdata.customerservice.domain.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nttdata.customerservice.api.dto.CustomerCreateRequestDto;
import com.nttdata.customerservice.api.dto.CustomerUpdateRequestDto;
import com.nttdata.customerservice.domain.policy.CustomerValidationPolicy;
import com.nttdata.customerservice.domain.port.CustomerRecord;
import com.nttdata.customerservice.domain.port.CustomerRepositoryPort;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepositoryPort customerRepositoryPort;

    private CustomerServiceImpl customerService;

    @BeforeEach
    void setUp() {
        customerService = new CustomerServiceImpl(new CustomerValidationPolicy(), customerRepositoryPort);
    }

    @Test
    void createShouldPersistCustomer() {
        CustomerCreateRequestDto request = new CustomerCreateRequestDto();
        request.setFirstName("Ana");
        request.setLastName("Perez");
        request.setDocumentType("DNI");
        request.setDocumentNumber("87654321");
        request.setEmail("ana@bank.local");
        request.setPhone("999111222");

        CustomerRecord saved = new CustomerRecord(
                "CUS-1",
                "Ana",
                "Perez",
                "DNI",
                "87654321",
                "ana@bank.local",
                "999111222",
                "ACTIVE",
                "demo.user",
                Instant.now(),
                Instant.now());

        when(customerRepositoryPort.findByDocumentNumber("87654321")).thenReturn(Mono.empty());
        when(customerRepositoryPort.save(any(CustomerRecord.class))).thenReturn(Mono.just(saved));

        StepVerifier.create(customerService.create(request, "demo.user"))
                .assertNext(response -> {
                    assertThat(response.getCustomerId()).isEqualTo("CUS-1");
                    assertThat(response.getDocumentNumber()).isEqualTo("87654321");
                    assertThat(response.getCreatedBy()).isEqualTo("demo.user");
                })
                .verifyComplete();

        verify(customerRepositoryPort).save(any(CustomerRecord.class));
    }

    @Test
    void findAllShouldReturnEmptyWhenRepositoryFails() {
        when(customerRepositoryPort.findAll()).thenReturn(Flux.error(new IllegalStateException("mongo down")));

        StepVerifier.create(customerService.findAll(null))
                .verifyComplete();
    }

    @Test
    void updateShouldReturnUpdatedCustomer() {
        CustomerRecord existing = new CustomerRecord(
                "CUS-1",
                "Ana",
                "Perez",
                "DNI",
                "87654321",
                "ana@bank.local",
                "999111222",
                "ACTIVE",
                "demo.user",
                Instant.now(),
                Instant.now());

        CustomerUpdateRequestDto request = new CustomerUpdateRequestDto();
        request.setFirstName("Ana Maria");
        request.setLastName("Perez");
        request.setEmail("anamaria@bank.local");
        request.setPhone("999000111");
        request.setStatus("ACTIVE");

        when(customerRepositoryPort.findById("CUS-1")).thenReturn(Mono.just(existing));
        when(customerRepositoryPort.save(any(CustomerRecord.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(customerService.update("CUS-1", request, "demo.user"))
                .assertNext(response -> {
                    assertThat(response.getFirstName()).isEqualTo("Ana Maria");
                    assertThat(response.getEmail()).isEqualTo("anamaria@bank.local");
                })
                .verifyComplete();
    }
}