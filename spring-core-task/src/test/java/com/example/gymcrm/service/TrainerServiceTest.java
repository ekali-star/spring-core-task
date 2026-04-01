package com.example.gymcrm.service;

import com.example.gymcrm.dto.Auth;
import com.example.gymcrm.metric.UserMetrics;
import com.example.gymcrm.model.Trainer;
import com.example.gymcrm.model.TrainingType;
import com.example.gymcrm.model.User;
import com.example.gymcrm.repository.TrainerRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock private TrainerRepository trainerRepository;
    @Mock private UserMetrics userMetrics;

    private TrainerService trainerService;

    private Trainer trainer;
    private User user;
    private Auth validAuth;

    @BeforeEach
    void setUp() {
        trainerService = new TrainerService(trainerRepository, userMetrics);

        user = new User(1L, "Mike", "Smith", "mike.smith", "password", true);

        trainer = new Trainer();
        trainer.setId(1L);
        trainer.setUser(user);
        trainer.setSpecialization(new TrainingType(1L, "Cardio"));

        validAuth = new Auth("mike.smith", "password");
    }

    @Test
    void updateTrainer_success() {
        Trainer updated = new Trainer();
        updated.setUser(new User(null, "Michael", "Johnson", null, null, true));

        when(trainerRepository.findByUserUsername("mike.smith"))
                .thenReturn(Optional.of(trainer));
        when(trainerRepository.save(trainer)).thenReturn(trainer);

        Trainer result = trainerService.updateTrainer(validAuth, "mike.smith", updated);

        assertEquals("Michael", result.getUser().getFirstName());
        assertEquals("Johnson", result.getUser().getLastName());
    }

    @Test
    void updateTrainer_authFail() {
        when(trainerRepository.findByUserUsername("mike.smith"))
                .thenReturn(Optional.of(trainer));

        assertThrows(IllegalArgumentException.class,
                () -> trainerService.updateTrainer(new Auth("mike.smith", "wrong"), "mike.smith", new Trainer()));
    }
}