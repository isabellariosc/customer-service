package com.nttdata.customerservice.domain.mapper;

import com.nttdata.customerservice.api.dto.CustomerCreateRequestDto;
import com.nttdata.customerservice.api.dto.CustomerResponseDto;
import com.nttdata.customerservice.api.dto.CustomerUpdateRequestDto;
import com.nttdata.customerservice.domain.port.CustomerRecord;
import com.nttdata.customerservice.generated.model.CreateCustomerRequest;
import com.nttdata.customerservice.generated.model.CustomerResponse;
import com.nttdata.customerservice.generated.model.UpdateCustomerRequest;
import java.time.Instant;
import java.util.UUID;

public final class CustomerMapper {

    private CustomerMapper() {
    }

    public static CustomerCreateRequestDto toCreateDto(CreateCustomerRequest request) {
        CustomerCreateRequestDto dto = new CustomerCreateRequestDto();
        dto.setFirstName(request.getFirstName());
        dto.setLastName(request.getLastName());
        dto.setDocumentType(request.getDocumentType());
        dto.setDocumentNumber(request.getDocumentNumber());
        dto.setEmail(request.getEmail());
        dto.setPhone(request.getPhone());
        return dto;
    }

    public static CustomerUpdateRequestDto toUpdateDto(UpdateCustomerRequest request) {
        CustomerUpdateRequestDto dto = new CustomerUpdateRequestDto();
        dto.setFirstName(request.getFirstName());
        dto.setLastName(request.getLastName());
        dto.setEmail(request.getEmail());
        dto.setPhone(request.getPhone());
        dto.setStatus(request.getStatus());
        return dto;
    }

    public static CustomerRecord toNewRecord(CustomerCreateRequestDto request, String requestedBy) {
        Instant now = Instant.now();
        return new CustomerRecord(
                "CUS-" + UUID.randomUUID(),
                request.getFirstName(),
                request.getLastName(),
                request.getDocumentType(),
                request.getDocumentNumber(),
                request.getEmail(),
                request.getPhone(),
                "ACTIVE",
                requestedBy,
                now,
                now);
    }

    public static CustomerRecord merge(CustomerRecord current, CustomerUpdateRequestDto request) {
        return new CustomerRecord(
                current.customerId(),
                request.getFirstName(),
                request.getLastName(),
                current.documentType(),
                current.documentNumber(),
                request.getEmail(),
                request.getPhone(),
                request.getStatus() == null || request.getStatus().isBlank() ? current.status() : request.getStatus(),
                current.createdBy(),
                current.createdAt(),
                Instant.now());
    }

    public static CustomerResponseDto toDto(CustomerRecord customerRecord) {
        return CustomerResponseDto.builder()
                .customerId(customerRecord.customerId())
                .firstName(customerRecord.firstName())
                .lastName(customerRecord.lastName())
                .documentType(customerRecord.documentType())
                .documentNumber(customerRecord.documentNumber())
                .email(customerRecord.email())
                .phone(customerRecord.phone())
                .status(customerRecord.status())
                .createdBy(customerRecord.createdBy())
                .build();
    }

    public static CustomerResponse toGenerated(CustomerResponseDto dto) {
        return new CustomerResponse()
                .customerId(dto.getCustomerId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .documentType(dto.getDocumentType())
                .documentNumber(dto.getDocumentNumber())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .status(dto.getStatus())
                .createdBy(dto.getCreatedBy());
    }
}