package com.example.trainerworkload.repository;

import com.example.trainerworkload.model.TrainerSummary;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TrainerSummaryRepository {

    private final Map<String, TrainerSummary> store = new ConcurrentHashMap<>();

    public Optional<TrainerSummary> findByUsername(String username) {
        return Optional.ofNullable(store.get(username));
    }

    public void save(TrainerSummary summary) {
        store.put(summary.getUsername(), summary);
    }
}