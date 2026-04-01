package com.example.gymcrm.controller;

import com.example.gymcrm.facade.GymFacade;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TrainerController.class)
class TrainerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GymFacade facade;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getTrainer_success() throws Exception {
        mockMvc.perform(get("/api/trainers")
                        .param("username", "trainer")
                        .header("authUsername", "trainer")
                        .header("authPassword", "pass"))
                .andExpect(status().isOk());
    }

    @Test
    void getTrainer_missingHeader() throws Exception {
        mockMvc.perform(get("/api/trainers")
                        .param("username", "trainer"))
                .andExpect(status().isBadRequest());
    }
}