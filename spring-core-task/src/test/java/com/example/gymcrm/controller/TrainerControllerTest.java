package com.example.gymcrm.controller;

import com.example.gymcrm.dto.AuthCredentials;
import com.example.gymcrm.dto.request.*;
import com.example.gymcrm.dto.response.TrainerProfileResponse;
import com.example.gymcrm.dto.response.TrainingResponse;
import com.example.gymcrm.facade.GymFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
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
    void register_success() throws Exception {
        TrainerRegistrationRequest req = new TrainerRegistrationRequest();
        req.setFirstName("Mike");
        req.setLastName("Smith");

        when(facade.createTrainer(any()))
                .thenReturn(new AuthCredentials("mike", "pass"));

        mockMvc.perform(post("/api/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("mike"));

        verify(facade).createTrainer(any());
    }

    @Test
    void register_validationFail() throws Exception {
        mockMvc.perform(post("/api/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getTrainer_success() throws Exception {
        TrainerProfileResponse response = new TrainerProfileResponse();
        response.setUsername("mike");

        when(facade.getTrainerByUsername(any(), eq("mike")))
                .thenReturn(response);

        mockMvc.perform(get("/api/trainers")
                        .param("username", "mike")
                        .header("authUsername", "mike")
                        .header("authPassword", "pass"))
                .andExpect(status().isOk());

        verify(facade).getTrainerByUsername(any(), eq("mike"));
    }

    @Test
    void getTrainer_missingHeader() throws Exception {
        mockMvc.perform(get("/api/trainers")
                        .param("username", "mike"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateTrainer_success() throws Exception {
        UpdateTrainerRequest req = new UpdateTrainerRequest();
        req.setUsername("mike");
        req.setFirstName("Mike");
        req.setLastName("Updated");

        TrainerProfileResponse response = new TrainerProfileResponse();
        response.setUsername("mike");

        when(facade.updateTrainer(any(), eq("mike"), any()))
                .thenReturn(response);

        mockMvc.perform(put("/api/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                        .header("authUsername", "mike")
                        .header("authPassword", "pass"))
                .andExpect(status().isOk());

        verify(facade).updateTrainer(any(), eq("mike"), any());
    }

    @Test
    void activateTrainer_success() throws Exception {
        ActivateRequest req = new ActivateRequest();
        req.setUsername("mike");
        req.setIsActive(true);

        doNothing().when(facade).setTrainerActiveStatus(any(), eq("mike"), eq(true));

        mockMvc.perform(patch("/api/trainers/activate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                        .header("authUsername", "mike")
                        .header("authPassword", "pass"))
                .andExpect(status().isOk());

        verify(facade).setTrainerActiveStatus(any(), eq("mike"), eq(true));
    }

    @Test
    void getTrainings_success() throws Exception {
        List<TrainingResponse> trainings = Arrays.asList(new TrainingResponse());

        when(facade.getTrainerTrainings(any(), eq("mike"), any(), any(), any()))
                .thenReturn(trainings);

        mockMvc.perform(get("/api/trainers/trainings")
                        .param("username", "mike")
                        .header("authUsername", "mike")
                        .header("authPassword", "pass"))
                .andExpect(status().isOk());

        verify(facade).getTrainerTrainings(any(), eq("mike"), any(), any(), any());
    }
}