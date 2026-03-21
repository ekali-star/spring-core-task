package com.example.gymcrm.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddTrainingRequest {
    private String traineeUsername;
    private String trainerUsername;

    private String trainingName;
    private Long trainingTypeId;
    private java.time.LocalDate trainingDate;
    private Integer trainingDuration;
}