package com.example.gymcrm.service;

import com.example.gymcrm.dto.Auth;
import com.example.gymcrm.metric.TrainingMetrics;
import com.example.gymcrm.model.*;
import com.example.gymcrm.repository.TrainingRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @Mock private TrainingRepository trainingRepository;
    @Mock private TraineeService traineeService;
    @Mock private TrainerService trainerService;
    @Mock private TrainingMetrics trainingMetrics;

    private TrainingService trainingService;

    private Trainee trainee;
    private Trainer trainer;
    private Training training;
    private Auth traineeAuth;
    private Auth trainerAuth;

    @BeforeEach
    void setUp() {
        trainingService = new TrainingService(
                trainingRepository, traineeService, trainerService, trainingMetrics);

        User traineeUser = new User(1L, "John", "Doe", "john.doe", "pass", true);
        trainee = new Trainee(1L, LocalDate.now(), "Address", traineeUser, List.of(), List.of());

        User trainerUser = new User(2L, "Mike", "Smith", "mike.smith", "pass", true);
        trainer = new Trainer();
        trainer.setUser(trainerUser);

        training = new Training(
                1L,
                trainee,
                trainer,
                "Training Name",
                new TrainingType(1L, "Cardio"),
                LocalDate.now(),
                60
        );
        training.setTrainingDate(LocalDate.now());

        traineeAuth = new Auth("john.doe", "pass");
        trainerAuth = new Auth("mike.smith", "pass");
    }

    @Test
    void createTraining_success() {
        when(traineeService.findByUsername("john.doe")).thenReturn(trainee);
        when(trainerService.findByUsername("mike.smith")).thenReturn(trainer);
        when(trainingRepository.save(any())).thenReturn(training);

        Training result = trainingService.createTraining("john.doe", "mike.smith", training);

        assertNotNull(result);
        verify(trainingMetrics).increment();
        verify(trainingRepository).save(training);
    }

    @Test
    void createTraining_traineeNotFound() {
        when(traineeService.findByUsername("john.doe")).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> trainingService.createTraining("john.doe", "mike.smith", training));
    }

    @Test
    void createTraining_inactiveUser() {
        trainee.getUser().setIsActive(false);

        when(traineeService.findByUsername("john.doe")).thenReturn(trainee);
        when(trainerService.findByUsername("mike.smith")).thenReturn(trainer);

        assertThrows(IllegalArgumentException.class,
                () -> trainingService.createTraining("john.doe", "mike.smith", training));
    }

    @Test
    void getTraineeTrainings_success() {
        when(traineeService.authenticate(traineeAuth)).thenReturn(true);
        when(trainingRepository.findTraineeTrainings(any(), any(), any(), any(), any()))
                .thenReturn(List.of(training));

        List<Training> result = trainingService.getTraineeTrainings(
                traineeAuth, null, null, null, null);

        assertEquals(1, result.size());
    }

    @Test
    void getTrainerTrainings_success() {
        when(trainerService.authenticate(trainerAuth)).thenReturn(true);
        when(trainingRepository.findTrainerTrainings(any(), any(), any(), any()))
                .thenReturn(List.of(training));

        List<Training> result = trainingService.getTrainerTrainings(
                trainerAuth, null, null, null);

        assertEquals(1, result.size());
    }

    @Test
    void findById() {
        when(trainingRepository.findById(1L)).thenReturn(Optional.of(training));

        assertTrue(trainingService.findById(1L).isPresent());
    }

    @Test
    void findAll() {
        when(trainingRepository.findAll()).thenReturn(List.of(training));

        assertEquals(1, trainingService.findAll().size());
    }
}