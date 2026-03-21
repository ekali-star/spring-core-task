package com.example.gymcrm.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTrainerRequest {
    private String username;
    private String password;

    private String firstName;
    private String lastName;
    private Boolean isActive;
}