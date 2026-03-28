package com.nttdata.customerservice.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CustomerCreateRequestDto {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String documentType;

    @NotBlank
    private String documentNumber;

    @Email
    @NotBlank
    private String email;

    private String phone;
}