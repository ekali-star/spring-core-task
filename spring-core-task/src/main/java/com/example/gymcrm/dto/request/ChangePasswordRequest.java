package com.example.gymcrm.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {
    private String username;
    private String oldPassword;
    private String newPassword;
}