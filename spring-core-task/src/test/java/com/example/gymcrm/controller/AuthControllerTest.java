package com.example.gymcrm.controller;

import com.example.gymcrm.dto.request.ChangePasswordRequest;
import com.example.gymcrm.dto.request.LoginRequest;
import com.example.gymcrm.facade.GymFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private GymFacade facade;

    private AuthController controller;

    @BeforeEach
    void setUp() {
        controller = new AuthController(facade);
    }

    @Test
    void login_ShouldReturnOk() {
        String username = "john.doe";
        String password = "password";

        ResponseEntity<Void> response = controller.login(username, password);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(facade).login(any(LoginRequest.class));
    }

    @Test
    void changePassword_ShouldReturnOk() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setUsername("john.doe");
        request.setOldPassword("oldPass");
        request.setNewPassword("newPass");

        ResponseEntity<Void> response = controller.changePassword(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(facade).changePassword(request);
    }
}