package com.nttdata.customerservice.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.nttdata.customerservice.domain.port.CustomerRecord;
import com.nttdata.customerservice.domain.port.CustomerRepositoryPort;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class CustomerApiIntegrationTest {

    private static final String JWT_SECRET = "change_this_secret_in_real_env_min_32_chars";

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CustomerRepositoryPort customerRepositoryPort;

    @Test
    void createCustomerShouldValidateTokenAndReturnCreated() {
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

        webTestClient.post()
                .uri("/api/customers")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenFor("demo.user"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          "firstName": "Ana",
                          "lastName": "Perez",
                          "documentType": "DNI",
                          "documentNumber": "87654321",
                          "email": "ana@bank.local",
                          "phone": "999111222"
                        }
                        """)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.customerId").isEqualTo("CUS-1")
                .jsonPath("$.createdBy").isEqualTo("demo.user");
    }

    private String tokenFor(String subject) {
        SecretKey key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(subject)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(30, ChronoUnit.MINUTES)))
                .signWith(key)
                .compact();
    }
}