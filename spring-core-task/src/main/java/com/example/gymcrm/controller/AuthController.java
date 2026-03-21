package com.example.gymcrm.controller;

import com.example.gymcrm.dto.request.ChangePasswordRequest;
import com.example.gymcrm.facade.GymFacade;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final GymFacade facade;

    public AuthController(GymFacade facade) {
        this.facade = facade;
    }

    @GetMapping("/login")
    public void login(@RequestParam String username,
                      @RequestParam String password) {

        boolean success =
                facade.authenticateTrainee(username, password) ||
                        facade.authenticateTrainer(username, password);

        if (!success) {
            throw new IllegalArgumentException("Invalid credentials");
        }
    }

    @PutMapping("/password")
    public void changePassword(@RequestBody ChangePasswordRequest req) {
        facade.changeTraineePassword(req.getUsername(), req.getOldPassword(), req.getNewPassword());
    }
}