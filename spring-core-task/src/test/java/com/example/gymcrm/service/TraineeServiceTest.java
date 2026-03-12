package com.example.gymcrm.service;

import com.example.gymcrm.dto.Auth;
import com.example.gymcrm.model.*;
import com.example.gymcrm.repository.TraineeRepository;
import com.example.gymcrm.repository.TrainerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TraineeServiceTest {

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainerRepository trainerRepository;

    private TraineeService traineeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        traineeService = new TraineeService(traineeRepository, trainerRepository);
    }

    @Test
    void getUnassignedTrainers_shouldReturnList() {
        TraineeService spyService = spy(new TraineeService(traineeRepository, trainerRepository));

        Auth auth = new Auth("john", "pass");

        doReturn(true).when(spyService).authenticate(auth);

        when(trainerRepository.findNotAssignedToTrainee("john"))
                .thenReturn(List.of(mock(Trainer.class), mock(Trainer.class)));

        List<Trainer> trainers = spyService.getUnassignedTrainers(auth);

        assertEquals(2, trainers.size());
    }

    @Test
    void updateTrainers_shouldThrowException_ifTraineeNotFound() {
        when(traineeRepository.findByUser_Username("john"))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> traineeService.updateTrainers(new Auth("john", "pass"), List.of("trainer1")));
    }
}