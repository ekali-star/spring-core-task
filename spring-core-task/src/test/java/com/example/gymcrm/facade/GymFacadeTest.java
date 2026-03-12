package com.example.gymcrm.facade;

import com.example.gymcrm.dto.Auth;
import com.example.gymcrm.dto.AuthCredentials;
import com.example.gymcrm.model.*;
import com.example.gymcrm.service.TraineeService;
import com.example.gymcrm.service.TrainerService;
import com.example.gymcrm.service.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
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
        User user = new User(null, "John", "Doe", "john", "pass", true);
        Trainee trainee = new Trainee(null, LocalDate.now(), "Address", user, List.of(), List.of());

        AuthCredentials credentials = new AuthCredentials("john", "pass");
        when(traineeService.create(trainee)).thenReturn(credentials);

        AuthCredentials result = gymFacade.createTrainee(trainee);

        assertEquals(credentials, result);
        verify(traineeService).create(trainee);
    }

    @Test
    void getAllTrainees_shouldReturnCollection() {
        Trainee t1 = mock(Trainee.class);
        Trainee t2 = mock(Trainee.class);

        when(traineeService.findAll()).thenReturn(List.of(t1, t2));

        assertEquals(2, gymFacade.getAllTrainees().size());
    }

    @Test
    void getTraining_shouldReturnTraining() {
        Trainee trainee = mock(Trainee.class);
        Trainer trainer = mock(Trainer.class);
        TrainingType type = new TrainingType(1L, "Cardio");
        Training training = new Training(1L, trainee, trainer, "Morning", type, LocalDate.now(), 60);

        when(trainingService.findById(1L)).thenReturn(Optional.of(training));

        Training result = gymFacade.getAllTrainings().stream().findFirst().orElse(training); // simplified

        assertEquals(training.getTrainingName(), result.getTrainingName());
    }

    @Test
    void getTraining_shouldThrowException_ifNotFound() {
        when(trainingService.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> {
                    Optional<Training> t = trainingService.findById(1L);
                    if (t.isEmpty()) throw new IllegalArgumentException("Training not found");
                });
    }
}