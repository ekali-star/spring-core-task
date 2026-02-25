package com.example.gymcrm.facade;

import com.example.gymcrm.model.Trainee;
import com.example.gymcrm.model.Trainer;
import com.example.gymcrm.model.Training;
import com.example.gymcrm.service.TraineeService;
import com.example.gymcrm.service.TrainerService;
import com.example.gymcrm.service.TrainingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GymFacadeTest {

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private GymFacade gymFacade;

    @Test
    void createTrainee_ShouldDelegateToService() {
        Trainee trainee = new Trainee();
        when(traineeService.create(trainee)).thenReturn(trainee);

        Trainee result = gymFacade.createTrainee(trainee);

        assertNotNull(result);
        verify(traineeService).create(trainee);
        verifyNoMoreInteractions(traineeService, trainerService, trainingService);
    }

    @Test
    void updateTrainee_ShouldDelegateToService() {
        Long id = 1L;
        Trainee trainee = new Trainee();
        when(traineeService.update(id, trainee)).thenReturn(trainee);

        Trainee result = gymFacade.updateTrainee(id, trainee);

        assertEquals(trainee, result);
        verify(traineeService).update(id, trainee);
        verifyNoMoreInteractions(traineeService, trainerService, trainingService);
    }

    @Test
    void deleteTrainee_ShouldDelegateToService() {
        Long id = 1L;

        gymFacade.deleteTrainee(id);

        verify(traineeService).delete(id);
        verifyNoMoreInteractions(traineeService, trainerService, trainingService);
    }

    @Test
    void getTrainee_ShouldDelegateToService() {
        Long id = 1L;
        Trainee trainee = new Trainee();
        when(traineeService.findById(id)).thenReturn(trainee);

        Trainee result = gymFacade.getTrainee(id);

        assertEquals(trainee, result);
        verify(traineeService).findById(id);
        verifyNoMoreInteractions(traineeService, trainerService, trainingService);
    }

    @Test
    void createTrainer_ShouldDelegateToService() {
        Trainer trainer = new Trainer();
        when(trainerService.create(trainer)).thenReturn(trainer);

        Trainer result = gymFacade.createTrainer(trainer);

        assertNotNull(result);
        verify(trainerService).create(trainer);
        verifyNoMoreInteractions(traineeService, trainerService, trainingService);
    }

    @Test
    void updateTrainer_ShouldDelegateToService() {
        Long id = 1L;
        Trainer trainer = new Trainer();
        when(trainerService.update(id, trainer)).thenReturn(trainer);

        Trainer result = gymFacade.updateTrainer(id, trainer);

        assertEquals(trainer, result);
        verify(trainerService).update(id, trainer);
        verifyNoMoreInteractions(traineeService, trainerService, trainingService);
    }

    @Test
    void getTrainer_ShouldDelegateToService() {
        Long id = 1L;
        Trainer trainer = new Trainer();
        when(trainerService.findById(id)).thenReturn(trainer);

        Trainer result = gymFacade.getTrainer(id);

        assertEquals(trainer, result);
        verify(trainerService).findById(id);
        verifyNoMoreInteractions(traineeService, trainerService, trainingService);
    }

    @Test
    void createTraining_ShouldDelegateToService() {
        Training training = new Training();
        when(trainingService.create(training)).thenReturn(training);

        Training result = gymFacade.createTraining(training);

        assertNotNull(result);
        verify(trainingService).create(training);
        verifyNoMoreInteractions(traineeService, trainerService, trainingService);
    }

    @Test
    void getTraining_ShouldDelegateToService() {
        Long id = 1L;
        Training training = new Training();
        when(trainingService.findById(id)).thenReturn(training);

        Training result = gymFacade.getTraining(id);

        assertEquals(training, result);
        verify(trainingService).findById(id);
        verifyNoMoreInteractions(traineeService, trainerService, trainingService);
    }
}