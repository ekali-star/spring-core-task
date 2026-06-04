package com.example.trainerworkload.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TrainerWorkloadRequest {

    @NotBlank
    private String trainerUsername;

    @NotBlank
    private String trainerFirstName;

    @NotBlank
    private String trainerLastName;

    private boolean isActive;

    @NotNull
    private LocalDate trainingDate;

    private int trainingDuration;

    @NotNull
    private ActionType actionType;

    public enum ActionType {
        ADD, DELETE
    }
}