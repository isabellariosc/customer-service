package com.nttdata.customerservice.generated.api;

import com.nttdata.customerservice.generated.model.CreateCustomerRequest;
import com.nttdata.customerservice.generated.model.CustomerResponse;
import com.nttdata.customerservice.generated.model.UpdateCustomerRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.http.codec.multipart.Part;

import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import jakarta.annotation.Generated;

/**
 * A delegate to be called by the {@link CustomerApiController}}.
 * Implement this interface with a {@link org.springframework.stereotype.Service} annotated class.
 */
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-27T16:22:09.449758600-05:00[America/Lima]", comments = "Generator version: 7.5.0")
public interface CustomerApiDelegate {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * POST /api/customers : Create customer
     *
     * @param createCustomerRequest  (required)
     * @return Customer created (status code 201)
     *         or Invalid request (status code 400)
     *         or Customer already exists (status code 409)
     * @see CustomerApi#createCustomer
     */
    default Mono<ResponseEntity<CustomerResponse>> createCustomer(Mono<CreateCustomerRequest> createCustomerRequest,
        ServerWebExchange exchange) {
        Mono<Void> result = Mono.empty();
        exchange.getResponse().setStatusCode(HttpStatus.NOT_IMPLEMENTED);
        for (MediaType mediaType : exchange.getRequest().getHeaders().getAccept()) {
            if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                String exampleString = "{ \"firstName\" : \"firstName\", \"lastName\" : \"lastName\", \"documentType\" : \"documentType\", \"phone\" : \"phone\", \"createdBy\" : \"createdBy\", \"documentNumber\" : \"documentNumber\", \"customerId\" : \"customerId\", \"email\" : \"email\", \"status\" : \"status\" }";
                result = ApiUtil.getExampleResponse(exchange, MediaType.valueOf("application/json"), exampleString);
                break;
            }
        }
        return result.then(createCustomerRequest).then(Mono.empty());

    }

    /**
     * DELETE /api/customers/{customerId} : Delete customer
     *
     * @param customerId  (required)
     * @return Customer deleted (status code 204)
     *         or Customer not found (status code 404)
     * @see CustomerApi#deleteCustomer
     */
    default Mono<ResponseEntity<Void>> deleteCustomer(String customerId,
        ServerWebExchange exchange) {
        Mono<Void> result = Mono.empty();
        exchange.getResponse().setStatusCode(HttpStatus.NOT_IMPLEMENTED);
        return result.then(Mono.empty());

    }

    /**
     * GET /api/customers/{customerId} : Get customer by id
     *
     * @param customerId  (required)
     * @return Customer found (status code 200)
     *         or Customer not found (status code 404)
     * @see CustomerApi#getCustomerById
     */
    default Mono<ResponseEntity<CustomerResponse>> getCustomerById(String customerId,
        ServerWebExchange exchange) {
        Mono<Void> result = Mono.empty();
        exchange.getResponse().setStatusCode(HttpStatus.NOT_IMPLEMENTED);
        for (MediaType mediaType : exchange.getRequest().getHeaders().getAccept()) {
            if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                String exampleString = "{ \"firstName\" : \"firstName\", \"lastName\" : \"lastName\", \"documentType\" : \"documentType\", \"phone\" : \"phone\", \"createdBy\" : \"createdBy\", \"documentNumber\" : \"documentNumber\", \"customerId\" : \"customerId\", \"email\" : \"email\", \"status\" : \"status\" }";
                result = ApiUtil.getExampleResponse(exchange, MediaType.valueOf("application/json"), exampleString);
                break;
            }
        }
        return result.then(Mono.empty());

    }

    /**
     * GET /api/customers : List customers
     *
     * @param documentNumber  (optional)
     * @return Customers retrieved (status code 200)
     * @see CustomerApi#getCustomers
     */
    default Mono<ResponseEntity<Flux<CustomerResponse>>> getCustomers(String documentNumber,
        ServerWebExchange exchange) {
        Mono<Void> result = Mono.empty();
        exchange.getResponse().setStatusCode(HttpStatus.NOT_IMPLEMENTED);
        for (MediaType mediaType : exchange.getRequest().getHeaders().getAccept()) {
            if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                String exampleString = "[ { \"firstName\" : \"firstName\", \"lastName\" : \"lastName\", \"documentType\" : \"documentType\", \"phone\" : \"phone\", \"createdBy\" : \"createdBy\", \"documentNumber\" : \"documentNumber\", \"customerId\" : \"customerId\", \"email\" : \"email\", \"status\" : \"status\" }, { \"firstName\" : \"firstName\", \"lastName\" : \"lastName\", \"documentType\" : \"documentType\", \"phone\" : \"phone\", \"createdBy\" : \"createdBy\", \"documentNumber\" : \"documentNumber\", \"customerId\" : \"customerId\", \"email\" : \"email\", \"status\" : \"status\" } ]";
                result = ApiUtil.getExampleResponse(exchange, MediaType.valueOf("application/json"), exampleString);
                break;
            }
        }
        return result.then(Mono.empty());

    }

    /**
     * PUT /api/customers/{customerId} : Update customer
     *
     * @param customerId  (required)
     * @param updateCustomerRequest  (required)
     * @return Customer updated (status code 200)
     *         or Customer not found (status code 404)
     * @see CustomerApi#updateCustomer
     */
    default Mono<ResponseEntity<CustomerResponse>> updateCustomer(String customerId,
        Mono<UpdateCustomerRequest> updateCustomerRequest,
        ServerWebExchange exchange) {
        Mono<Void> result = Mono.empty();
        exchange.getResponse().setStatusCode(HttpStatus.NOT_IMPLEMENTED);
        for (MediaType mediaType : exchange.getRequest().getHeaders().getAccept()) {
            if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                String exampleString = "{ \"firstName\" : \"firstName\", \"lastName\" : \"lastName\", \"documentType\" : \"documentType\", \"phone\" : \"phone\", \"createdBy\" : \"createdBy\", \"documentNumber\" : \"documentNumber\", \"customerId\" : \"customerId\", \"email\" : \"email\", \"status\" : \"status\" }";
                result = ApiUtil.getExampleResponse(exchange, MediaType.valueOf("application/json"), exampleString);
                break;
            }
        }
        return result.then(updateCustomerRequest).then(Mono.empty());

    }

}
