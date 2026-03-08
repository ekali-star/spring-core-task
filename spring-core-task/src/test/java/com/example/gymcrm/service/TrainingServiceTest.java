package com.example.gymcrm.service;

import com.example.gymcrm.model.*;
import com.example.gymcrm.repository.TrainingRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainingServiceTest {

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    private TrainingService trainingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        trainingService = new TrainingService(trainingRepository, traineeService, trainerService);
    }

    private Training prepareTrainingMock() {

        Training training = mock(Training.class);
        Trainee trainee = mock(Trainee.class);
        Trainer trainer = mock(Trainer.class);
        User traineeUser = new User("John","Doe","john","pass",true);
        User trainerUser = new User("Mike","Smith","mike","pass",true);

        when(training.getTrainee()).thenReturn(trainee);
        when(training.getTrainer()).thenReturn(trainer);

        when(trainee.getUser()).thenReturn(traineeUser);
        when(trainer.getUser()).thenReturn(trainerUser);

        return training;
    }

    @Test
    void createTraining_shouldSaveTraining_whenAuthenticated() {

        Training training = prepareTrainingMock();

        when(traineeService.authenticate(any(), any())).thenReturn(true);
        when(trainerService.authenticate(any(), any())).thenReturn(true);
        when(trainingRepository.save(training)).thenReturn(training);

        Training result = trainingService.createTraining("trainee","trainer",training);

        assertEquals(training, result);
        verify(trainingRepository).save(training);
    }

    @Test
    void createTraining_shouldThrowException_whenAuthFails() {

        Training training = prepareTrainingMock();

        when(traineeService.authenticate(any(), any())).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> trainingService.createTraining("trainee","trainer",training));
    }

    @Test
    void findById_shouldReturnTraining() {

        Training training = mock(Training.class);

        when(trainingRepository.findById(1L)).thenReturn(Optional.of(training));

        Optional<Training> result = trainingService.findById(1L);

        assertTrue(result.isPresent());
    }
}