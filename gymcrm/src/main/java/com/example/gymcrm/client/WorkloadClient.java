package com.example.gymcrm.client;

import com.example.gymcrm.dto.request.TrainerWorkloadRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class WorkloadClient {

    private final RestTemplate restTemplate;
    private final CircuitBreakerFactory circuitBreakerFactory;
    private final OAuth2TokenProvider tokenProvider;

    private static final String WORKLOAD_URL =
        "http://trainer-workload/api/trainer-workload";

    public void sendWorkload(TrainerWorkloadRequest request) {
        CircuitBreaker cb = circuitBreakerFactory.create("trainerWorkload");
        cb.run(
            () -> {
                HttpHeaders headers = new HttpHeaders();
                headers.setBearerAuth(tokenProvider.getToken());
                headers.setContentType(MediaType.APPLICATION_JSON);

                String txId = MDC.get("transactionId");
                if (txId != null) {
                    headers.set("X-Transaction-Id", txId);
                }

                HttpEntity<TrainerWorkloadRequest> entity =
                    new HttpEntity<>(request, headers);
                restTemplate.postForEntity(WORKLOAD_URL, entity, Void.class);
                log.info("Workload sent for trainer={}",
                    request.getTrainerUsername());
                return null;
            },
            throwable -> {
                log.error("Workload service unavailable: {}",
                    throwable.getMessage());
                return null;
            }
        );
    }
}