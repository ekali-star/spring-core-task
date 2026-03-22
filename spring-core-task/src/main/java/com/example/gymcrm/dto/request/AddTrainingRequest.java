package com.example.gymcrm.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AddTrainingRequest {
    private String traineeUsername;
    private String trainerUsername;

    private String trainingName;
    private Long trainingTypeId;
    private LocalDate trainingDate;
    private Integer trainingDuration;
}