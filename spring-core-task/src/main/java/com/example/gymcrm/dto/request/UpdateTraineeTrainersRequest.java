package com.example.gymcrm.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateTraineeTrainersRequest {
    private String username;
    private String password;
    private List<String> trainerUsernames;
}