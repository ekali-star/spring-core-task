package com.example.gymcrm.service;

import com.example.gymcrm.dao.TraineeDao;
import com.example.gymcrm.model.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @Mock
    private TraineeDao traineeDao;

    @InjectMocks
    private TraineeService traineeService;

    private Trainee trainee;

    @BeforeEach
    void setUp() {
        trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
        trainee.setDateOfBirth(LocalDate.of(1990, 1, 1));
        trainee.setAddress("123 Main St");
    }

    @Test
    void create_ShouldGenerateUsernameAndPassword() {
        when(traineeDao.findAll()).thenReturn(Collections.emptyList());

        Trainee result = traineeService.create(trainee);

        assertNotNull(result.getId());
        assertEquals("John.Doe", result.getUsername());
        assertNotNull(result.getPassword());
        assertEquals(10, result.getPassword().length());
        assertTrue(result.isActive());
        verify(traineeDao).save(anyLong(), any(Trainee.class));
    }

    @Test
    void create_ShouldGenerateUniqueUsername_WhenDuplicateExists() {
        Trainee existing = new Trainee();
        existing.setUsername("John.Doe");
        when(traineeDao.findAll()).thenReturn(Collections.singletonList(existing));

        Trainee result = traineeService.create(trainee);

        assertEquals("John.Doe1", result.getUsername());
        verify(traineeDao).save(anyLong(), any(Trainee.class));
    }

    @Test
    void update_ShouldUpdateExistingTrainee() {
        Long id = 1L;
        Trainee existing = new Trainee();
        existing.setId(id);
        existing.setUsername("John.Doe");
        existing.setPassword("oldpass");

        when(traineeDao.findById(id)).thenReturn(existing);

        traineeService.update(id, trainee);

        verify(traineeDao).save(eq(id), eq(trainee));
    }

    @Test
    void update_ShouldThrowException_WhenTraineeNotFound() {
        Long id = 1L;
        when(traineeDao.findById(id)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> traineeService.update(id, trainee));
    }

    @Test
    void delete_ShouldDeleteTrainee() {
        Long id = 1L;
        Trainee existing = new Trainee();
        existing.setId(id);
        when(traineeDao.findById(id)).thenReturn(existing);

        traineeService.delete(id);

        verify(traineeDao).delete(id);
    }

    @Test
    void delete_ShouldNotThrow_WhenTraineeNotFound() {
        Long id = 1L;
        when(traineeDao.findById(id)).thenReturn(null);

        traineeService.delete(id);

        verify(traineeDao, never()).delete(id);
    }

    @Test
    void findById_ShouldReturnTrainee() {
        Long id = 1L;
        when(traineeDao.findById(id)).thenReturn(trainee);

        Trainee result = traineeService.findById(id);

        assertEquals(trainee, result);
    }

    @Test
    void findAll_ShouldReturnAllTrainees() {
        Collection<Trainee> trainees = Arrays.asList(trainee, new Trainee());
        when(traineeDao.findAll()).thenReturn(trainees);

        Collection<Trainee> result = traineeService.findAll();

        assertEquals(2, result.size());
    }
}