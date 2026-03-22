package com.example.gymcrm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TraineeProfileResponse {
    private String username;
    private String firstName;
    private String lastName;
    private java.time.LocalDate dateOfBirth;
    private String address;
    private Boolean isActive;
    private List<TrainerSummaryDTO> trainers;
}
