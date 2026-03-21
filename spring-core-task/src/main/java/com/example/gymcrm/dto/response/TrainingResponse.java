package com.example.gymcrm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TrainingResponse {
    private String trainingName;
    private java.time.LocalDate trainingDate;
    private String trainingType;
    private Integer trainingDuration;
    private String partnerName;
}
