package com.example.gymcrm.controller;

import com.example.gymcrm.dto.request.TraineeRegistrationRequest;
import com.example.gymcrm.dto.AuthCredentials;
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

@WebMvcTest(TraineeController.class)
class TraineeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GymFacade facade;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void register_success() throws Exception {
        TraineeRegistrationRequest req = new TraineeRegistrationRequest();
        req.setFirstName("John");
        req.setLastName("Doe");

        when(facade.createTrainee(any()))
                .thenReturn(new AuthCredentials("john", "pass"));

        mockMvc.perform(post("/api/trainees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john"));

        verify(facade).createTrainee(any());
    }

    @Test
    void register_validationFail() throws Exception {
        mockMvc.perform(post("/api/trainees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_serviceException() throws Exception {
        when(facade.createTrainee(any()))
                .thenThrow(new RuntimeException());

        TraineeRegistrationRequest req = new TraineeRegistrationRequest();
        req.setFirstName("John");
        req.setLastName("Doe");

        mockMvc.perform(post("/api/trainees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getProfile_success() throws Exception {
        mockMvc.perform(get("/api/trainees")
                        .param("username", "john")
                        .header("authUsername", "john")
                        .header("authPassword", "pass"))
                .andExpect(status().isOk());
    }

    @Test
    void getProfile_missingHeader() throws Exception {
        mockMvc.perform(get("/api/trainees")
                        .param("username", "john"))
                .andExpect(status().isBadRequest());
    }
}