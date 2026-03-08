package com.example.gymcrm.facade;

import com.example.gymcrm.model.Trainee;
import com.example.gymcrm.model.Trainer;
import com.example.gymcrm.model.Training;
import com.example.gymcrm.service.TraineeService;
import com.example.gymcrm.service.TrainerService;
import com.example.gymcrm.service.TrainingService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GymFacadeTest {

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TrainingService trainingService;

    private GymFacade gymFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        gymFacade = new GymFacade(traineeService, trainerService, trainingService);
    }

    @Test
    void createTrainee_shouldCallService() {

        Trainee trainee = mock(Trainee.class);

        when(traineeService.create(trainee)).thenReturn(trainee);

        Trainee result = gymFacade.createTrainee(trainee);

        assertEquals(trainee, result);
        verify(traineeService).create(trainee);
    }

    @Test
    void getAllTrainees_shouldReturnCollection() {

        when(traineeService.findAll())
                .thenReturn(List.of(mock(Trainee.class), mock(Trainee.class)));

        assertEquals(2, gymFacade.getAllTrainees().size());
    }

    @Test
    void getTraining_shouldReturnTraining() {

        Training training = mock(Training.class);

        when(trainingService.findById(1L)).thenReturn(Optional.of(training));

        Training result = gymFacade.getTraining(1L);

        assertEquals(training, result);
    }

    @Test
    void getTraining_shouldThrowException_ifNotFound() {

        when(trainingService.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> gymFacade.getTraining(1L));
    }
}