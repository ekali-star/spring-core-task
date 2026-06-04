package com.example.trainerworkload.service;

import com.example.trainerworkload.dto.TrainerWorkloadRequest;
import com.example.trainerworkload.model.TrainerSummary;
import com.example.trainerworkload.repository.TrainerSummaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.example.trainerworkload.dto.TrainerWorkloadRequest.ActionType.ADD;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainerWorkloadService {

    private final TrainerSummaryRepository repository;

    public void processWorkload(TrainerWorkloadRequest request) {
        log.debug("Processing workload: trainer={} action={}",
            request.getTrainerUsername(), request.getActionType());

        TrainerSummary summary = repository
            .findByUsername(request.getTrainerUsername())
            .orElseGet(() -> {
                TrainerSummary s = new TrainerSummary();
                s.setUsername(request.getTrainerUsername());
                return s;
            });

        summary.setFirstName(request.getTrainerFirstName());
        summary.setLastName(request.getTrainerLastName());
        summary.setActive(request.isActive());

        int year = request.getTrainingDate().getYear();
        int month = request.getTrainingDate().getMonthValue();

        summary.getYearMonthDuration()
            .computeIfAbsent(year, y -> new HashMap<>());

        Map<Integer, Integer> monthMap =
            summary.getYearMonthDuration().get(year);

        if (request.getActionType() == ADD) {
            monthMap.merge(month, request.getTrainingDuration(), Integer::sum);
            log.debug("Added {} mins for trainer={} year={} month={}",
                request.getTrainingDuration(),
                request.getTrainerUsername(), year, month);
        } else {
            int current = monthMap.getOrDefault(month, 0);
            int updated = current - request.getTrainingDuration();
            if (updated <= 0) {
                monthMap.remove(month);
            } else {
                monthMap.put(month, updated);
            }
            log.debug("Removed {} mins for trainer={} year={} month={}",
                request.getTrainingDuration(),
                request.getTrainerUsername(), year, month);
        }

        repository.save(summary);
    }

    public Optional<TrainerSummary> getTrainerSummary(String username) {
        return repository.findByUsername(username);
    }
}