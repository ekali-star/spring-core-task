package com.example.gymcrm.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainerRegistrationRequest {
    private String firstName;
    private String lastName;
    private Long specializationId;
}