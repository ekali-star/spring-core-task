package com.example.gymcrm.dto.request;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class TraineeRegistrationRequest {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;
}