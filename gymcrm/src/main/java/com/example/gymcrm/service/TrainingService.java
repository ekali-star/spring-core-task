package com.example.gymcrm.service;

import com.example.gymcrm.client.WorkloadClient;
import com.example.gymcrm.dto.Auth;
import com.example.gymcrm.dto.request.TrainerWorkloadRequest;
import com.example.gymcrm.metric.TrainingMetrics;
import com.example.gymcrm.model.Trainee;
import com.example.gymcrm.model.Trainer;
import com.example.gymcrm.model.Training;
import com.example.gymcrm.repository.TrainingRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.example.gymcrm.dto.request.TrainerWorkloadRequest.ActionType.ADD;
import static com.example.gymcrm.dto.request.TrainerWorkloadRequest.ActionType.DELETE;

@Service
@Transactional
public class TrainingService {

    private final TrainingRepository trainingRepository;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingMetrics trainingMetrics;
    private final WorkloadClient workloadClient;

    public TrainingService(TrainingRepository trainingRepository,
                           TraineeService traineeService,
                           TrainerService trainerService,
                           TrainingMetrics trainingMetrics,
                           WorkloadClient workloadClient) {
        this.trainingRepository = trainingRepository;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingMetrics = trainingMetrics;
        this.workloadClient = workloadClient;
    }

    public Training createTraining(String traineeUsername,
                                   String trainerUsername,
                                   Training training) {
        Trainee trainee = traineeService.findByUsername(traineeUsername);
        if (trainee == null)
            throw new IllegalArgumentException("Trainee not found: " + traineeUsername);

        Trainer trainer = trainerService.findByUsername(trainerUsername);
        if (trainer == null)
            throw new IllegalArgumentException("Trainer not found: " + trainerUsername);

        if (!trainee.getUser().getIsActive() || !trainer.getUser().getIsActive()) {
            throw new IllegalArgumentException("Trainee or Trainer is not active");
        }

        training.setTrainee(trainee);
        training.setTrainer(trainer);
        trainingMetrics.increment();

        Training saved = trainingRepository.save(training);

        workloadClient.sendWorkload(buildRequest(saved, ADD));

        return saved;
    }

    public void deleteTraining(Long trainingId) {
        Training training = trainingRepository.findById(trainingId)
            .orElseThrow(() -> new EntityNotFoundException(
                "Training not found: " + trainingId));

        trainingRepository.delete(training);

        workloadClient.sendWorkload(buildRequest(training, DELETE));
    }

    private TrainerWorkloadRequest buildRequest(Training training,
                                                TrainerWorkloadRequest.ActionType action) {
        TrainerWorkloadRequest req = new TrainerWorkloadRequest();
        req.setTrainerUsername(training.getTrainer().getUser().getUsername());
        req.setTrainerFirstName(training.getTrainer().getUser().getFirstName());
        req.setTrainerLastName(training.getTrainer().getUser().getLastName());
        req.setActive(training.getTrainer().getUser().getIsActive());
        req.setTrainingDate(training.getTrainingDate());
        req.setTrainingDuration(training.getTrainingDuration());
        req.setActionType(action);
        return req;
    }

    public List<Training> getTraineeTrainings(String username, LocalDate fromDate,
                                              LocalDate toDate, String trainerName,
                                              Long trainingTypeId) {
        return trainingRepository.findTraineeTrainings(username, fromDate,
            toDate, trainerName, trainingTypeId);
    }

    public List<Training> getTrainerTrainings(String username, LocalDate fromDate,
                                              LocalDate toDate, String traineeName) {
        return trainingRepository.findTrainerTrainings(username, fromDate,
            toDate, traineeName);
    }

    public Optional<Training> findById(Long id) {
        return trainingRepository.findById(id);
    }

    public List<Training> findAll() {
        return trainingRepository.findAll();
    }
}