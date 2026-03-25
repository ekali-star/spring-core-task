package com.example.gymcrm.controller;

import com.example.gymcrm.dto.request.AddTrainingRequest;
import com.example.gymcrm.facade.GymFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingControllerTest {

    @Mock
    private GymFacade facade;

    private TrainingController controller;

    @BeforeEach
    void setUp() {
        controller = new TrainingController(facade);
    }

    @Test
    void add_ShouldReturnOk() {
        AddTrainingRequest request = new AddTrainingRequest();
        request.setTraineeUsername("john.doe");
        request.setTrainerUsername("mike.smith");
        request.setTrainingName("Morning Run");
        request.setTrainingDate(LocalDate.now());
        request.setTrainingDuration(60);

        ResponseEntity<Void> response = controller.add(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(facade).createTraining(request);
    }
}