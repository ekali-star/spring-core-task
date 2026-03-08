package com.example.gymcrm.service;

import com.example.gymcrm.model.Trainer;
import com.example.gymcrm.model.User;
import com.example.gymcrm.repository.TrainerRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerServiceTest {

    @Mock
    private TrainerRepository trainerRepository;

    private TrainerService trainerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        trainerService = new TrainerService(trainerRepository);
    }

    @Test
    void findAll_shouldReturnAllTrainers() {

        when(trainerRepository.findAll())
                .thenReturn(List.of(mock(Trainer.class), mock(Trainer.class)));

        assertEquals(2, trainerService.findAll().size());
    }

    @Test
    void findById_shouldReturnTrainer() {

        Trainer trainer = mock(Trainer.class);

        when(trainerRepository.findById(1L))
                .thenReturn(Optional.of(trainer));

        Trainer result = trainerService.findById(1L);

        assertEquals(trainer, result);
    }

    @Test
    void delete_shouldCallRepositoryDelete() {

        Trainer trainer = mock(Trainer.class);
        User user = new User("John","Doe","john","pass",true);

        when(trainer.getUser()).thenReturn(user);
        when(trainerRepository.findById(1L))
                .thenReturn(Optional.of(trainer));

        trainerService.delete(1L);

        verify(trainerRepository).deleteById(1L);
    }

    @Test
    void authenticate_shouldReturnTrue_whenPasswordMatches() {

        Trainer trainer = mock(Trainer.class);
        User user = new User("John","Doe","john","pass",true);

        when(trainer.getUser()).thenReturn(user);
        when(trainerRepository.findByUser_Username("john"))
                .thenReturn(Optional.of(trainer));

        boolean result = trainerService.authenticate("john","pass");

        assertTrue(result);
    }

    @Test
    void authenticate_shouldReturnFalse_whenPasswordWrong() {

        Trainer trainer = mock(Trainer.class);
        User user = new User("John","Doe","john","pass",true);

        when(trainer.getUser()).thenReturn(user);
        when(trainerRepository.findByUser_Username("john"))
                .thenReturn(Optional.of(trainer));

        boolean result = trainerService.authenticate("john","wrong");

        assertFalse(result);
    }
}