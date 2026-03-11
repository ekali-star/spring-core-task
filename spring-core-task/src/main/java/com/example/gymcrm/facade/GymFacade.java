package com.example.gymcrm.facade;

import com.example.gymcrm.dto.Auth;
import com.example.gymcrm.dto.AuthCredentials;
import com.example.gymcrm.model.*;
import com.example.gymcrm.service.TraineeService;
import com.example.gymcrm.service.TrainerService;
import com.example.gymcrm.service.TrainingService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Component
public class GymFacade {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    public GymFacade(
            TraineeService traineeService,
            TrainerService trainerService,
            TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    public AuthCredentials createTrainee(Trainee trainee) {
        return traineeService.create(trainee);
    }

    public AuthCredentials createTrainer(Trainer trainer) {
        return trainerService.create(trainer);
    }

    public Training createTraining(String traineeUsername, String trainerUsername, Training training) {
        return trainingService.createTraining(traineeUsername, trainerUsername, training);
    }

    public Trainee getTraineeById(Long id) {
        return traineeService.findById(id);
    }

    public Trainee getTraineeByUsername(String username) {
        return traineeService.findByUsername(username);
    }

    public Collection<Trainee> getAllTrainees() {
        return traineeService.findAll();
    }

    public Trainer getTrainerById(Long id) {
        return trainerService.findById(id);
    }

    public Trainer getTrainerByUsername(String username) {
        return trainerService.findByUsername(username);
    }

    public Collection<Trainer> getAllTrainers() {
        return trainerService.findAll();
    }

    public Training getTraining(Long id) {
        return trainingService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Training not found with id: " + id));
    }

    public Collection<Training> getAllTrainings() {
        return trainingService.findAll();
    }

    public List<Training> getTraineeTrainings(String username, String password,
                                              LocalDate fromDate, LocalDate toDate,
                                              String trainerName, Long trainingTypeId) {
        Auth auth = new Auth(username, password);
        return trainingService.getTraineeTrainings(auth, fromDate, toDate, trainerName, trainingTypeId);
    }

    public List<Training> getTrainerTrainings(String username, String password,
                                              LocalDate fromDate, LocalDate toDate,
                                              String traineeName) {
        Auth auth = new Auth(username, password);
        return trainingService.getTrainerTrainings(auth, fromDate, toDate, traineeName);
    }

    public List<Trainer> getUnassignedTrainers(String username, String password) {
        if (!traineeService.authenticate(username, password)) {
            throw new IllegalArgumentException("Authentication failed");
        }
        return traineeService.getUnassignedTrainers(username);
    }

    public Trainee updateTrainee(String username, String password, Trainee trainee) {
        Auth auth = new Auth(username, password);
        return traineeService.updateTrainee(auth, trainee);
    }

    public Trainer updateTrainer(String username, String password, Trainer trainer) {
        Auth auth = new Auth(username, password);
        return trainerService.updateTrainer(auth, trainer);
    }

    public void updateTraineeTrainers(String username, String password, List<String> trainerUsernames) {
        Auth auth = new Auth(username, password);
        traineeService.updateTrainers(auth, trainerUsernames);
    }

    public void deleteTrainee(String username, String password) {
        Auth auth = new Auth(username, password);
        traineeService.deleteTrainee(auth);
    }

    public void changeTraineePassword(String username, String oldPassword, String newPassword) {
        if (!traineeService.authenticate(username, oldPassword)) {
            throw new IllegalArgumentException("Authentication failed");
        }
        traineeService.changePassword(username, newPassword);
    }

    public void changeTrainerPassword(String username, String oldPassword, String newPassword) {
        if (!trainerService.authenticate(username, oldPassword)) {
            throw new IllegalArgumentException("Authentication failed");
        }
        trainerService.changePassword(username, newPassword);
    }

    public void activateTrainee(String username, String password) {
        if (!traineeService.authenticate(username, password)) {
            throw new IllegalArgumentException("Authentication failed");
        }
        traineeService.setActiveStatus(username, true);
    }

    public void deactivateTrainee(String username, String password) {
        if (!traineeService.authenticate(username, password)) {
            throw new IllegalArgumentException("Authentication failed");
        }
        traineeService.setActiveStatus(username, false);
    }

    public void activateTrainer(String username, String password) {
        if (!trainerService.authenticate(username, password)) {
            throw new IllegalArgumentException("Authentication failed");
        }
        trainerService.setActiveStatus(username, true);
    }

    public void deactivateTrainer(String username, String password) {
        if (!trainerService.authenticate(username, password)) {
            throw new IllegalArgumentException("Authentication failed");
        }
        trainerService.setActiveStatus(username, false);
    }

    public boolean authenticateTrainee(String username, String password) {
        return traineeService.authenticate(username, password);
    }

    public boolean authenticateTrainer(String username, String password) {
        return trainerService.authenticate(username, password);
    }

    @Deprecated
    public Trainee updateTrainee(Long id, Trainee trainee) {
        throw new UnsupportedOperationException(
                "Use updateTrainee(String username, String password, Trainee trainee) instead"
        );
    }

    @Deprecated
    public void deleteTrainee(Long id) {
        throw new UnsupportedOperationException(
                "Use deleteTrainee(String username, String password) instead"
        );
    }

    @Deprecated
    public Trainer updateTrainer(Long id, Trainer trainer) {
        throw new UnsupportedOperationException(
                "Use updateTrainer(String username, String password, Trainer trainer) instead"
        );
    }
}