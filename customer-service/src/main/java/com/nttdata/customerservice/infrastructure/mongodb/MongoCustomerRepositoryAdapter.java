package com.nttdata.customerservice.infrastructure.mongodb;

import com.nttdata.customerservice.domain.port.CustomerRecord;
import com.nttdata.customerservice.domain.port.CustomerRepositoryPort;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class MongoCustomerRepositoryAdapter implements CustomerRepositoryPort {

    private final CustomerMongoRepository customerMongoRepository;

    public MongoCustomerRepositoryAdapter(CustomerMongoRepository customerMongoRepository) {
        this.customerMongoRepository = customerMongoRepository;
    }

    @Override
    public Mono<CustomerRecord> save(CustomerRecord customerRecord) {
        return customerMongoRepository.save(toDocument(customerRecord)).map(this::toRecord);
    }

    @Override
    public Mono<CustomerRecord> findById(String customerId) {
        return customerMongoRepository.findById(customerId).map(this::toRecord);
    }

    @Override
    public Mono<CustomerRecord> findByDocumentNumber(String documentNumber) {
        return customerMongoRepository.findByDocumentNumber(documentNumber).map(this::toRecord);
    }

    @Override
    public Flux<CustomerRecord> findAll() {
        return customerMongoRepository.findAll().map(this::toRecord);
    }

    @Override
    public Mono<Void> deleteById(String customerId) {
        return customerMongoRepository.deleteById(customerId);
    }

    private CustomerDocument toDocument(CustomerRecord customerRecord) {
        CustomerDocument document = new CustomerDocument();
        document.setId(customerRecord.customerId());
        document.setFirstName(customerRecord.firstName());
        document.setLastName(customerRecord.lastName());
        document.setDocumentType(customerRecord.documentType());
        document.setDocumentNumber(customerRecord.documentNumber());
        document.setEmail(customerRecord.email());
        document.setPhone(customerRecord.phone());
        document.setStatus(customerRecord.status());
        document.setCreatedBy(customerRecord.createdBy());
        document.setCreatedAt(customerRecord.createdAt());
        document.setUpdatedAt(customerRecord.updatedAt());
        return document;
    }

    private CustomerRecord toRecord(CustomerDocument document) {
        return new CustomerRecord(
                document.getId(),
                document.getFirstName(),
                document.getLastName(),
                document.getDocumentType(),
                document.getDocumentNumber(),
                document.getEmail(),
                document.getPhone(),
                document.getStatus(),
                document.getCreatedBy(),
                document.getCreatedAt(),
                document.getUpdatedAt());
    }
}