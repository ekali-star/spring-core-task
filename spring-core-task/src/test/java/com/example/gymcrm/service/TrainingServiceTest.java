package com.example.gymcrm.service;

import com.example.gymcrm.model.*;
import com.example.gymcrm.repository.TrainingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
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
        Trainee trainee = new Trainee();
        Trainer trainer = new Trainer();
        User traineeUser = new User(null, "John", "Doe", "john", "pass", true);
        User trainerUser = new User(null, "Mike", "Smith", "mike", "pass", true);
        trainee.setUser(traineeUser);
        trainer.setUser(trainerUser);
        TrainingType type = new TrainingType(1L, "Cardio");

        return new Training(1L, trainee, trainer, "Morning Cardio", type, LocalDate.now(), 60);
    }

    @Test
    void createTraining_shouldSaveTraining() {
        Training training = prepareTrainingMock();

        when(traineeService.findByUsername("trainee")).thenReturn(training.getTrainee());
        when(trainerService.findByUsername("trainer")).thenReturn(training.getTrainer());
        when(trainingRepository.save(training)).thenReturn(training);

        Training result = trainingService.createTraining("trainee", "trainer", training);

        assertEquals(training, result);
        verify(trainingRepository).save(training);
    }

    @Test
    void findById_shouldReturnTraining() {
        Training training = prepareTrainingMock();
        when(trainingRepository.findById(1L)).thenReturn(Optional.of(training));

        Optional<Training> result = trainingService.findById(1L);
        assertTrue(result.isPresent());
        assertEquals(training, result.get());
    }
}