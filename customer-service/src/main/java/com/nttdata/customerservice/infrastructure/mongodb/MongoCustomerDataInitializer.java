package com.nttdata.customerservice.infrastructure.mongodb;

import java.time.Instant;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class MongoCustomerDataInitializer {

    @Bean
    @ConditionalOnProperty(name = "customer.seed.enabled", havingValue = "true", matchIfMissing = true)
    ApplicationRunner customerDataInitializer(CustomerMongoRepository customerMongoRepository) {
        return args -> customerMongoRepository.findByDocumentNumber("87654321")
                .switchIfEmpty(Mono.defer(() -> {
                    CustomerDocument customer = new CustomerDocument();
                    customer.setId("CUS-DEMO");
                    customer.setFirstName("Ana");
                    customer.setLastName("Perez");
                    customer.setDocumentType("DNI");
                    customer.setDocumentNumber("87654321");
                    customer.setEmail("ana.perez@bank.local");
                    customer.setPhone("999111222");
                    customer.setStatus("ACTIVE");
                    customer.setCreatedBy("system");
                    customer.setCreatedAt(Instant.now());
                    customer.setUpdatedAt(Instant.now());
                    return customerMongoRepository.save(customer);
                }))
                .then()
                .subscribe();
    }
}