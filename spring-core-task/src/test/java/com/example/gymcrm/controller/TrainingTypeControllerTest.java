package com.example.gymcrm.controller;

import com.example.gymcrm.dto.response.TrainingTypeResponse;
import com.example.gymcrm.facade.GymFacade;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TrainingTypeController.class)
class TrainingTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GymFacade facade;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAll_success() throws Exception {
        List<TrainingTypeResponse> expected = List.of(
                new TrainingTypeResponse(1L, "Cardio"),
                new TrainingTypeResponse(2L, "Strength"),
                new TrainingTypeResponse(3L, "Yoga")
        );

        when(facade.getAllTrainingTypes()).thenReturn(expected);

        mockMvc.perform(get("/api/training-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Cardio"));

        verify(facade).getAllTrainingTypes();
    }

    @Test
    void getAll_emptyList() throws Exception {
        when(facade.getAllTrainingTypes()).thenReturn(List.of());

        mockMvc.perform(get("/api/training-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(facade).getAllTrainingTypes();
    }

    @Test
    void getAll_serviceException() throws Exception {
        when(facade.getAllTrainingTypes())
                .thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/training-types"))
                .andExpect(status().isInternalServerError());
    }
}