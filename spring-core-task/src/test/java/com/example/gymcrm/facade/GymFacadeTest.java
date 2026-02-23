package com.example.gymcrm.facade;

import com.example.gymcrm.model.*;
import com.example.gymcrm.service.TraineeService;
import com.example.gymcrm.service.TrainerService;
import com.example.gymcrm.service.TrainingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

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
    }

    @Test
    void updateTrainee_ShouldDelegateToService() {
        Long id = 1L;
        Trainee trainee = new Trainee();
        when(traineeService.update(id, trainee)).thenReturn(trainee);

        Trainee result = gymFacade.updateTrainee(id, trainee);

        assertEquals(trainee, result);
        verify(traineeService).update(id, trainee);
    }

    @Test
    void deleteTrainee_ShouldDelegateToService() {
        Long id = 1L;

        gymFacade.deleteTrainee(id);

        verify(traineeService).delete(id);
    }

    @Test
    void getTrainee_ShouldDelegateToService() {
        Long id = 1L;
        Trainee trainee = new Trainee();
        when(traineeService.findById(id)).thenReturn(trainee);

        Trainee result = gymFacade.getTrainee(id);

        assertEquals(trainee, result);
    }

    @Test
    void getAllTrainees_ShouldDelegateToService() {
        List<Trainee> trainees = List.of(new Trainee(), new Trainee());
        when(traineeService.findAll()).thenReturn(trainees);

        var result = gymFacade.getAllTrainees();

        assertEquals(2, result.size());
    }

    @Test
    void createTrainer_ShouldDelegateToService() {
        Trainer trainer = new Trainer();
        when(trainerService.create(trainer)).thenReturn(trainer);

        Trainer result = gymFacade.createTrainer(trainer);

        assertNotNull(result);
        verify(trainerService).create(trainer);
    }

    @Test
    void updateTrainer_ShouldDelegateToService() {
        Long id = 1L;
        Trainer trainer = new Trainer();
        when(trainerService.update(id, trainer)).thenReturn(trainer);

        Trainer result = gymFacade.updateTrainer(id, trainer);

        assertEquals(trainer, result);
        verify(trainerService).update(id, trainer);
    }

    @Test
    void getTrainer_ShouldDelegateToService() {
        Long id = 1L;
        Trainer trainer = new Trainer();
        when(trainerService.findById(id)).thenReturn(trainer);

        Trainer result = gymFacade.getTrainer(id);

        assertEquals(trainer, result);
    }

    @Test
    void createTraining_ShouldDelegateToService() {
        Training training = new Training();
        when(trainingService.create(training)).thenReturn(training);

        Training result = gymFacade.createTraining(training);

        assertNotNull(result);
        verify(trainingService).create(training);
    }

    @Test
    void getTraining_ShouldDelegateToService() {
        Long id = 1L;
        Training training = new Training();
        when(trainingService.findById(id)).thenReturn(training);

        Training result = gymFacade.getTraining(id);

        assertEquals(training, result);
    }

    @Test
    void getTrainingsByTrainee_ShouldDelegateToService() {
        Long traineeId = 1L;
        List<Training> trainings = List.of(new Training());
        when(trainingService.findByTraineeId(traineeId)).thenReturn(trainings);

        var result = gymFacade.getTrainingsByTrainee(traineeId);

        assertEquals(1, result.size());
    }

    @Test
    void getTrainingsByTrainer_ShouldDelegateToService() {
        Long trainerId = 1L;
        List<Training> trainings = List.of(new Training());
        when(trainingService.findByTrainerId(trainerId)).thenReturn(trainings);

        var result = gymFacade.getTrainingsByTrainer(trainerId);

        assertEquals(1, result.size());
    }

    @Test
    void getTrainingsByDate_ShouldDelegateToService() {
        LocalDate date = LocalDate.now();
        List<Training> trainings = List.of(new Training());
        when(trainingService.findByDate(date)).thenReturn(trainings);

        var result = gymFacade.getTrainingsByDate(date);

        assertEquals(1, result.size());
    }
}