package com.example.gymcrm.service;

import com.example.gymcrm.dao.TrainingDao;
import com.example.gymcrm.model.Training;
import com.example.gymcrm.model.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @Mock
    private TrainingDao trainingDao;

    @InjectMocks
    private TrainingService trainingService;

    private Training training;

    @BeforeEach
    void setUp() {
        training = new Training();
        training.setId(1L);
        training.setTraineeId(1L);
        training.setTrainerId(1L);
        training.setTrainingName("Morning Yoga");
        training.setTrainingType(TrainingType.YOGA);
        training.setTrainingDate(LocalDate.now());
        training.setTrainingDuration(60);
    }

    @Test
    void create_ShouldSaveTraining() {
        Training result = trainingService.create(training);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(TrainingType.YOGA, result.getTrainingType());
        verify(trainingDao).save(anyLong(), any(Training.class));
    }

    @Test
    void findById_ShouldReturnTraining() {
        Long id = 1L;
        when(trainingDao.findById(id)).thenReturn(training);

        Training result = trainingService.findById(id);

        assertEquals(training, result);
        assertEquals(TrainingType.YOGA, result.getTrainingType());
    }

    @Test
    void findById_ShouldReturnNull_WhenNotFound() {
        Long id = 999L;
        when(trainingDao.findById(id)).thenReturn(null);

        Training result = trainingService.findById(id);

        assertNull(result);
    }

    @Test
    void findAll_ShouldReturnAllTrainings() {
        Collection<Training> trainings = Arrays.asList(training, new Training());
        when(trainingDao.findAll()).thenReturn(trainings);

        Collection<Training> result = trainingService.findAll();

        assertEquals(2, result.size());
    }

}