package com.example.gymcrm.controller;

import com.example.gymcrm.dto.AuthCredentials;
import com.example.gymcrm.dto.request.*;
import com.example.gymcrm.dto.response.TraineeProfileResponse;
import com.example.gymcrm.dto.response.TrainerSummaryDTO;
import com.example.gymcrm.dto.response.TrainingResponse;
import com.example.gymcrm.facade.GymFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
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
    void getProfile_success() throws Exception {
        TraineeProfileResponse response = new TraineeProfileResponse();
        response.setUsername("john");

        when(facade.getTraineeByUsername(any(), eq("john")))
                .thenReturn(response);

        mockMvc.perform(get("/api/trainees")
                        .param("username", "john")
                        .header("authUsername", "john")
                        .header("authPassword", "pass"))
                .andExpect(status().isOk());

        verify(facade).getTraineeByUsername(any(), eq("john"));
    }

    @Test
    void getProfile_missingHeader() throws Exception {
        mockMvc.perform(get("/api/trainees")
                        .param("username", "john"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateProfile_success() throws Exception {
        UpdateTraineeRequest req = new UpdateTraineeRequest();
        req.setUsername("john");
        req.setFirstName("John");
        req.setLastName("Updated");

        TraineeProfileResponse response = new TraineeProfileResponse();
        response.setUsername("john");

        when(facade.updateTrainee(any(), eq("john"), any()))
                .thenReturn(response);

        mockMvc.perform(put("/api/trainees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                        .header("authUsername", "john")
                        .header("authPassword", "pass"))
                .andExpect(status().isOk());

        verify(facade).updateTrainee(any(), eq("john"), any());
    }

    @Test
    void deleteTrainee_success() throws Exception {
        doNothing().when(facade).deleteTrainee(any(), eq("john"));

        mockMvc.perform(delete("/api/trainees")
                        .param("username", "john")
                        .header("authUsername", "john")
                        .header("authPassword", "pass"))
                .andExpect(status().isOk());

        verify(facade).deleteTrainee(any(), eq("john"));
    }

    @Test
    void activateTrainee_success() throws Exception {
        ActivateRequest req = new ActivateRequest();
        req.setUsername("john");
        req.setIsActive(true);

        doNothing().when(facade).setTraineeActiveStatus(any(), eq("john"), eq(true));

        mockMvc.perform(patch("/api/trainees/activate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                        .header("authUsername", "john")
                        .header("authPassword", "pass"))
                .andExpect(status().isOk());

        verify(facade).setTraineeActiveStatus(any(), eq("john"), eq(true));
    }

    @Test
    void getUnassignedTrainers_success() throws Exception {
        List<TrainerSummaryDTO> trainers = Arrays.asList(new TrainerSummaryDTO());

        when(facade.getUnassignedTrainers(any(), eq("john")))
                .thenReturn(trainers);

        mockMvc.perform(get("/api/trainees/unassigned-trainers")
                        .param("username", "john")
                        .header("authUsername", "john")
                        .header("authPassword", "pass"))
                .andExpect(status().isOk());

        verify(facade).getUnassignedTrainers(any(), eq("john"));
    }

    @Test
    void updateTrainers_success() throws Exception {
        UpdateTraineeTrainersRequest req = new UpdateTraineeTrainersRequest();
        req.setUsername("john");
        req.setTrainerUsernames(Arrays.asList("trainer1"));

        List<TrainerSummaryDTO> trainers = Arrays.asList(new TrainerSummaryDTO());

        when(facade.updateTraineeTrainers(any(), eq("john"), anyList()))
                .thenReturn(trainers);

        mockMvc.perform(put("/api/trainees/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                        .header("authUsername", "john")
                        .header("authPassword", "pass"))
                .andExpect(status().isOk());

        verify(facade).updateTraineeTrainers(any(), eq("john"), anyList());
    }

    @Test
    void getTrainings_success() throws Exception {
        List<TrainingResponse> trainings = Arrays.asList(new TrainingResponse());

        when(facade.getTraineeTrainings(any(), eq("john"), any(), any(), any(), any()))
                .thenReturn(trainings);

        mockMvc.perform(get("/api/trainees/trainings")
                        .param("username", "john")
                        .header("authUsername", "john")
                        .header("authPassword", "pass"))
                .andExpect(status().isOk());

        verify(facade).getTraineeTrainings(any(), eq("john"), any(), any(), any(), any());
    }
}