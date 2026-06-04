package com.example.trainerworkload.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class TrainerSummary {
    private String username;
    private String firstName;
    private String lastName;
    private boolean active;

    private Map<Integer, Map<Integer, Integer>> yearMonthDuration = new HashMap<>();
}