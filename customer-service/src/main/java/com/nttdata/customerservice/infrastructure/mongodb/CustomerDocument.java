package com.nttdata.customerservice.infrastructure.mongodb;

import java.time.Instant;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "customers")
public class CustomerDocument {

    @Id
    private String id;

    private String firstName;
    private String lastName;
    private String documentType;

    @Indexed(unique = true)
    private String documentNumber;

    private String email;
    private String phone;
    private String status;
    private String createdBy;
    private Instant createdAt;
    private Instant updatedAt;
}