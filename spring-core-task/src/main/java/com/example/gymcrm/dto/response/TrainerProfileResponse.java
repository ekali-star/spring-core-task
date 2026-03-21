package com.example.gymcrm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TrainerProfileResponse {
    private String username;
    private String firstName;
    private String lastName;
    private String specialization;
    private Boolean isActive;
    private java.util.List<TraineeSummaryDTO> trainees;
}
