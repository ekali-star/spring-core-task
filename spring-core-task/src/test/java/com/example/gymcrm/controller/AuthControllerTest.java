package com.example.gymcrm.controller;

import com.example.gymcrm.dto.request.ChangePasswordRequest;
import com.example.gymcrm.dto.request.LoginRequest;
import com.example.gymcrm.facade.GymFacade;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GymFacade facade;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void login_success() throws Exception {
        mockMvc.perform(get("/api/auth/login")
                        .param("username", "john")
                        .param("password", "pass"))
                .andExpect(status().isOk());

        verify(facade).login(any(LoginRequest.class));
    }

    @Test
    void login_missingParams_shouldFail() throws Exception {
        mockMvc.perform(get("/api/auth/login"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void changePassword_success() throws Exception {
        ChangePasswordRequest req = new ChangePasswordRequest();
        req.setUsername("john");
        req.setOldPassword("old");
        req.setNewPassword("new");

        mockMvc.perform(put("/api/auth/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());

        verify(facade).changePassword(any());
    }

    @Test
    void changePassword_invalidBody_shouldFail() throws Exception {
        mockMvc.perform(put("/api/auth/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

}