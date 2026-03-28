package com.nttdata.customerservice.domain.policy;

import com.nttdata.customerservice.api.dto.CustomerCreateRequestDto;
import com.nttdata.customerservice.api.dto.CustomerUpdateRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class CustomerValidationPolicy {

    public void validateCreate(CustomerCreateRequestDto request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer payload is required");
        }
        if (isBlank(request.getFirstName()) || isBlank(request.getLastName()) || isBlank(request.getDocumentType())
                || isBlank(request.getDocumentNumber()) || isBlank(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required customer fields");
        }
    }

    public void validateUpdate(String customerId, CustomerUpdateRequestDto request) {
        validateCustomerId(customerId);
        if (request == null || isBlank(request.getFirstName()) || isBlank(request.getLastName())
                || isBlank(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid customer update payload");
        }
    }

    public void validateCustomerId(String customerId) {
        if (isBlank(customerId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer id is required");
        }
    }

    public void validateRequestedBy(String requestedBy) {
        if (isBlank(requestedBy)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authenticated user is required");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}