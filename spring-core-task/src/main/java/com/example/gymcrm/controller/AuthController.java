package com.example.gymcrm.controller;

import com.example.gymcrm.dto.request.ChangePasswordRequest;
import com.example.gymcrm.dto.request.LoginRequest;
import com.example.gymcrm.facade.GymFacade;
import com.example.gymcrm.security.BruteForceProtectionService;
import com.example.gymcrm.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final GymFacade facade;
    private final JwtService jwtService;
    private final BruteForceProtectionService bruteForce;

    public AuthController(GymFacade facade, JwtService jwtService,
                          BruteForceProtectionService bruteForce) {
        this.facade = facade;
        this.jwtService = jwtService;
        this.bruteForce = bruteForce;
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username,
                                   @RequestParam String password) {
        if (bruteForce.isBlocked(username)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(Map.of("message", "Account temporarily locked. Try again in 5 minutes."));
        }

        try {
            facade.login(new LoginRequest(username, password));
            bruteForce.loginSucceeded(username);
            String token = jwtService.generateToken(username);
            return ResponseEntity.ok(Map.of("token", token));
        } catch (Exception e) {
            bruteForce.loginFailed(username);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid credentials"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtService.blacklist(authHeader.substring(7));
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        facade.changePassword(request);
        return ResponseEntity.ok().build();
    }
}