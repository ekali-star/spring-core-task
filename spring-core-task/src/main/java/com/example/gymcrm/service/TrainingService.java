package com.example.gymcrm.service;

import com.example.gymcrm.model.Training;
import com.example.gymcrm.repository.TrainingRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TrainingService {

    private final TrainingRepository trainingRepository;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private static final Logger log = LoggerFactory.getLogger(TrainingService.class);

    public TrainingService(TrainingRepository trainingRepository,
                           TraineeService traineeService,
                           TrainerService trainerService) {
        this.trainingRepository = trainingRepository;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
    }

    public Training createTraining(String traineeUsername, String trainerUsername, Training training) {
        if (!traineeService.authenticate(traineeUsername, training.getTrainee().getUser().getPassword()) ||
                !trainerService.authenticate(trainerUsername, training.getTrainer().getUser().getPassword())) {
            throw new IllegalArgumentException("Authentication failed for trainee or trainer");
        }

        Training saved = trainingRepository.save(training);
        log.info("Training created - ID: {}, Name: {}", saved.getId(), saved.getTrainingName());
        return saved;
    }

    public List<Training> getTraineeTrainings(String traineeUsername,
                                              LocalDate from,
                                              LocalDate to,
                                              String trainerName,
                                              Long typeId,
                                              String password) {
        if (!traineeService.authenticate(traineeUsername, password)) {
            throw new IllegalArgumentException("Authentication failed");
        }

        return trainingRepository.findTraineeTrainings(traineeUsername, from, to, trainerName, typeId);
    }

    public List<Training> getTrainerTrainings(String trainerUsername,
                                              LocalDate from,
                                              LocalDate to,
                                              String traineeName,
                                              String password) {
        if (!trainerService.authenticate(trainerUsername, password)) {
            throw new IllegalArgumentException("Authentication failed");
        }

        return trainingRepository.findTrainerTrainings(trainerUsername, from, to, traineeName);
    }

    public Optional<Training> findById(Long id) {
        return trainingRepository.findById(id);
    }

    public List<Training> findAll() {
        return trainingRepository.findAll();
    }
}