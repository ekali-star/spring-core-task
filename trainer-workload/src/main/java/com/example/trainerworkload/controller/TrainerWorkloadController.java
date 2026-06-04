package com.example.trainerworkload.controller;

import com.example.trainerworkload.dto.TrainerWorkloadRequest;
import com.example.trainerworkload.model.TrainerSummary;
import com.example.trainerworkload.service.TrainerWorkloadService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trainer-workload")
@RequiredArgsConstructor
@Slf4j
public class TrainerWorkloadController {

    private static final Logger TXN_LOG =
        LoggerFactory.getLogger("TRANSACTION_LOGGER");

    private final TrainerWorkloadService service;

    @PostMapping
    public ResponseEntity<Void> updateWorkload(
        @Valid @RequestBody TrainerWorkloadRequest request,
        HttpServletRequest httpRequest) {

        String txId = (String) httpRequest.getAttribute("transactionId");
        TXN_LOG.info("[TXN:{}] POST /api/trainer-workload | trainer={} action={}",
            txId, request.getTrainerUsername(), request.getActionType());

        service.processWorkload(request);

        TXN_LOG.info("[TXN:{}] Response: 200 OK", txId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}")
    public ResponseEntity<TrainerSummary> getWorkload(
        @PathVariable String username,
        HttpServletRequest httpRequest) {

        String txId = (String) httpRequest.getAttribute("transactionId");
        TXN_LOG.info("[TXN:{}] GET /api/trainer-workload/{}",
            txId, username);

        return service.getTrainerSummary(username)
            .map(summary -> {
                TXN_LOG.info("[TXN:{}] Response: 200 OK", txId);
                return ResponseEntity.ok(summary);
            })
            .orElseGet(() -> {
                TXN_LOG.warn("[TXN:{}] Response: 404 Not Found", txId);
                return ResponseEntity.notFound().build();
            });
    }
}