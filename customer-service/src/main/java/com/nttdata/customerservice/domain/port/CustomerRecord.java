package com.nttdata.customerservice.domain.port;

import java.time.Instant;

public record CustomerRecord(
        String customerId,
        String firstName,
        String lastName,
        String documentType,
        String documentNumber,
        String email,
        String phone,
        String status,
        String createdBy,
        Instant createdAt,
        Instant updatedAt) {
}