package com.example.gymcrm.service;

import com.example.gymcrm.dto.Auth;
import com.example.gymcrm.dto.AuthCredentials;
import com.example.gymcrm.metric.UserMetrics;
import com.example.gymcrm.model.Trainee;
import com.example.gymcrm.model.Trainer;
import com.example.gymcrm.model.User;
import com.example.gymcrm.repository.TraineeRepository;
import com.example.gymcrm.repository.TrainerRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @Mock private TraineeRepository traineeRepository;
    @Mock private TrainerRepository trainerRepository;
    @Mock private UserMetrics userMetrics;

    private TraineeService traineeService;

    private User user;
    private Trainee trainee;
    private Auth validAuth;

    @BeforeEach
    void setUp() {
        traineeService = new TraineeService(traineeRepository, trainerRepository, userMetrics);

        user = new User(1L, "John", "Doe", "john.doe", "password", true);
        trainee = new Trainee(1L, LocalDate.now(), "Address", user, new ArrayList<>(), new ArrayList<>());
        validAuth = new Auth("john.doe", "password");
    }

    @Test
    void updateTrainee_success() {
        Trainee updated = new Trainee(null, LocalDate.now(), "New Address",
                new User(null, "Jane", "Smith", null, null, true), null, null);

        when(traineeRepository.findByUserUsername("john.doe")).thenReturn(Optional.of(trainee));
        when(traineeRepository.save(any())).thenReturn(trainee);

        Trainee result = traineeService.updateTrainee(validAuth, "john.doe", updated);

        assertEquals("New Address", result.getAddress());
        assertEquals("Jane", result.getUser().getFirstName());
        verify(traineeRepository).save(trainee);
    }

    @Test
    void updateTrainee_authFail() {
        when(traineeRepository.findByUserUsername("john.doe")).thenReturn(Optional.of(trainee));

        assertThrows(IllegalArgumentException.class,
                () -> traineeService.updateTrainee(new Auth("john.doe", "wrong"), "john.doe", new Trainee()));
    }

    @Test
    void deleteTrainee_authVersion() {
        when(traineeRepository.findByUserUsername("john.doe")).thenReturn(Optional.of(trainee));

        traineeService.deleteTrainee(validAuth);

        verify(traineeRepository).delete(trainee);
    }

    @Test
    void deleteTrainee_byUsername() {
        when(traineeRepository.findByUserUsername("john.doe")).thenReturn(Optional.of(trainee));

        traineeService.deleteTrainee("john.doe");

        verify(traineeRepository).delete(trainee);
    }

    @Test
    void getUnassignedTrainers_success() {
        when(traineeRepository.findByUserUsername("john.doe")).thenReturn(Optional.of(trainee));
        when(trainerRepository.findNotAssignedToTrainee("john.doe"))
                .thenReturn(List.of(new Trainer()));

        List<Trainer> result = traineeService.getUnassignedTrainers(validAuth);

        assertEquals(1, result.size());
    }

    @Test
    void updateTrainers_success() {
        when(traineeRepository.findWithTrainersByUserUsername("john.doe"))
                .thenReturn(Optional.of(trainee));

        Trainer newTrainer = new Trainer();
        newTrainer.setUser(new User(2L, "New", "Trainer", "new", "pass", true));

        when(trainerRepository.findByUserUsernameIn(Set.of("new")))
                .thenReturn(List.of(newTrainer));

        traineeService.updateTrainers("john.doe", List.of("new"));

        verify(traineeRepository).save(trainee);
    }

    @Test
    void updateTrainers_trainerNotFound() {
        when(traineeRepository.findWithTrainersByUserUsername("john.doe"))
                .thenReturn(Optional.of(trainee));

        when(trainerRepository.findByUserUsernameIn(Set.of("ghost")))
                .thenReturn(List.of());

        assertThrows(IllegalArgumentException.class,
                () -> traineeService.updateTrainers("john.doe", List.of("ghost")));
    }
}