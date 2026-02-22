package com.example.gymcrm.service;

import com.example.gymcrm.dao.TrainerDao;
import com.example.gymcrm.model.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    private TrainerDao trainerDao;

    @InjectMocks
    private TrainerService trainerService;

    private Trainer trainer;

    @BeforeEach
    void setUp() {
        trainer = new Trainer();
        trainer.setFirstName("Jane");
        trainer.setLastName("Smith");
        trainer.setSpecialization("Yoga");
    }

    @Test
    void create_ShouldGenerateUsernameAndPassword() {
        when(trainerDao.findAll()).thenReturn(Collections.emptyList());

        Trainer result = trainerService.create(trainer);

        assertNotNull(result.getId());
        assertEquals("Jane.Smith", result.getUsername());
        assertNotNull(result.getPassword());
        assertEquals(10, result.getPassword().length());
        assertEquals("Yoga", result.getSpecialization());
        assertTrue(result.isActive());
        verify(trainerDao).save(anyLong(), any(Trainer.class));
    }

    @Test
    void create_ShouldGenerateUniqueUsername_WhenDuplicateExists() {
        Trainer existing = new Trainer();
        existing.setUsername("Jane.Smith");
        when(trainerDao.findAll()).thenReturn(Collections.singletonList(existing));

        Trainer result = trainerService.create(trainer);

        assertEquals("Jane.Smith1", result.getUsername());
        verify(trainerDao).save(anyLong(), any(Trainer.class));
    }

    @Test
    void update_ShouldUpdateExistingTrainer() {
        Long id = 1L;
        Trainer existing = new Trainer();
        existing.setId(id);
        existing.setUsername("Jane.Smith");
        existing.setPassword("oldpass");

        when(trainerDao.findById(id)).thenReturn(existing);

        trainerService.update(id, trainer);

        verify(trainerDao).save(eq(id), eq(trainer));
    }

    @Test
    void update_ShouldThrowException_WhenTrainerNotFound() {
        Long id = 1L;
        when(trainerDao.findById(id)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> trainerService.update(id, trainer));
    }

    @Test
    void findById_ShouldReturnTrainer() {
        Long id = 1L;
        when(trainerDao.findById(id)).thenReturn(trainer);

        Trainer result = trainerService.findById(id);

        assertEquals(trainer, result);
    }

    @Test
    void findAll_ShouldReturnAllTrainers() {
        Collection<Trainer> trainers = Arrays.asList(trainer, new Trainer());
        when(trainerDao.findAll()).thenReturn(trainers);

        Collection<Trainer> result = trainerService.findAll();

        assertEquals(2, result.size());
    }
}