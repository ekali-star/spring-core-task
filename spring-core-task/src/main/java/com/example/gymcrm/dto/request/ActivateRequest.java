package com.example.gymcrm.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivateRequest {
    private String username;
    private String password;
    private Boolean isActive;
}