package com.example.gymcrm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TraineeProfileResponse {
    private String username;
    private String firstName;
    private String lastName;
    private java.time.LocalDate dateOfBirth;
    private String address;
    private Boolean isActive;
    private java.util.List<TrainerSummaryDTO> trainers;
}
