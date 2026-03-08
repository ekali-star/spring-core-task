package com.example.gymcrm.service;

import com.example.gymcrm.model.Trainee;
import com.example.gymcrm.model.Trainer;
import com.example.gymcrm.repository.TraineeRepository;
import com.example.gymcrm.repository.TrainerRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

        when(trainerRepository.findNotAssignedToTrainee("john"))
                .thenReturn(List.of(mock(Trainer.class), mock(Trainer.class)));

        List<Trainer> trainers = traineeService.getUnassignedTrainers("john");

        assertEquals(2, trainers.size());
    }

    @Test
    void updateTrainers_shouldUpdateTrainerList() {

        Trainee trainee = mock(Trainee.class);

        when(traineeRepository.findByUser_Username("john"))
                .thenReturn(Optional.of(trainee));

        when(trainerRepository.findAllById(List.of(1L,2L)))
                .thenReturn(List.of(mock(Trainer.class), mock(Trainer.class)));

        traineeService.updateTrainers("john", List.of(1L,2L));

        verify(trainee).setTrainers(any());
        verify(traineeRepository).save(trainee);
    }

    @Test
    void updateTrainers_shouldThrowException_ifTraineeNotFound() {

        when(traineeRepository.findByUser_Username("john"))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> traineeService.updateTrainers("john", List.of(1L)));
    }
}