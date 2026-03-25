package com.example.gymcrm.controller;

import com.example.gymcrm.dto.request.ChangePasswordRequest;
import com.example.gymcrm.dto.request.LoginRequest;
import com.example.gymcrm.facade.GymFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final GymFacade facade;

    public AuthController(GymFacade facade) {
        this.facade = facade;
    }

    @GetMapping("/login")
    public ResponseEntity<Void> login(
            @RequestParam String username,
            @RequestParam String password) {

        facade.login(new LoginRequest(username, password));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(
            @RequestBody ChangePasswordRequest request) {

        facade.changePassword(request);
        return ResponseEntity.ok().build();
    }
}