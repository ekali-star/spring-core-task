package com.example.gymcrm.facade;

import com.example.gymcrm.dto.Auth;
import com.example.gymcrm.dto.AuthCredentials;
import com.example.gymcrm.dto.request.*;
import com.example.gymcrm.dto.response.*;
import com.example.gymcrm.model.*;
import com.example.gymcrm.service.TraineeService;
import com.example.gymcrm.service.TrainerService;
import com.example.gymcrm.service.TrainingService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GymFacade {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    public GymFacade(TraineeService traineeService,
                     TrainerService trainerService,
                     TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    public void login(LoginRequest req) {
        traineeService.authenticate(req.getUsername(), req.getPassword());
    }

    public void changePassword(ChangePasswordRequest req) {
        Auth auth = new Auth(req.getUsername(), req.getOldPassword());
        if (traineeService.authenticate(req.getUsername(), req.getOldPassword())) {
            traineeService.changePassword(auth, req.getNewPassword());
        } else {
            trainerService.changePassword(auth, req.getNewPassword());
        }
    }

    public AuthCredentials createTrainee(TraineeRegistrationRequest req) {
        User user = User.builder()
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .build();
        Trainee trainee = new Trainee();
        trainee.setUser(user);
        trainee.setDateOfBirth(req.getDateOfBirth());
        trainee.setAddress(req.getAddress());
        return traineeService.create(trainee);
    }

    public TraineeProfileResponse getTraineeByUsername(String username) {
        return toTraineeProfile(traineeService.findByUsername(username));
    }

    public TraineeProfileResponse updateTrainee(UpdateTraineeRequest req) {
        Auth auth = new Auth(req.getUsername(), req.getPassword());
        User user = User.builder()
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .build();
        Trainee patch = new Trainee();
        patch.setUser(user);
        patch.setDateOfBirth(req.getDateOfBirth());
        patch.setAddress(req.getAddress());
        return toTraineeProfile(traineeService.updateTrainee(auth, patch));
    }

    public void deleteTrainee(String username) {
        traineeService.deleteTrainee(username);
    }

    public void setTraineeActiveStatus(ActivateRequest req) {
        traineeService.setActiveStatus(req.getUsername(), req.getIsActive());
    }

    public List<TrainerSummaryDTO> getUnassignedTrainers(String username) {
        return traineeService.getUnassignedTrainers(username)
                .stream()
                .map(this::toTrainerSummary)
                .toList();
    }

    public List<TrainerSummaryDTO> updateTraineeTrainers(UpdateTraineeTrainersRequest req) {
        traineeService.updateTrainers(req.getUsername(), req.getTrainerUsernames());
        return getTraineeByUsername(req.getUsername()).getTrainers();
    }

    public List<TrainingResponse> getTraineeTrainings(TraineeTrainingQueryRequest req) {
        return trainingService.getTraineeTrainings(
                req.getUsername(),
                req.getPeriodFrom(),
                req.getPeriodTo(),
                req.getTrainerName(),
                req.getTrainingTypeId()
        ).stream().map(t -> new TrainingResponse(
                t.getTrainingName(),
                t.getTrainingDate(),
                t.getTrainingType().getTrainingTypeName(),
                t.getTrainingDuration(),
                t.getTrainer().getUser().getUsername()
        )).toList();
    }

    public AuthCredentials createTrainer(TrainerRegistrationRequest req) {
        User user = User.builder()
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .build();
        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setSpecialization(TrainingType.builder().id(req.getSpecializationId()).build());
        return trainerService.create(trainer);
    }

    public TrainerProfileResponse getTrainerByUsername(String username) {
        return toTrainerProfile(trainerService.findByUsername(username));
    }

    public TrainerProfileResponse updateTrainer(UpdateTrainerRequest req) {
        Auth auth = new Auth(req.getUsername(), req.getPassword());
        User user = User.builder()
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .build();
        Trainer patch = new Trainer();
        patch.setUser(user);
        return toTrainerProfile(trainerService.updateTrainer(auth, patch));
    }

    public void setTrainerActiveStatus(ActivateRequest req) {
        trainerService.setActiveStatus(req.getUsername(), req.getIsActive());
    }

    public List<TrainingResponse> getTrainerTrainings(TrainerTrainingQueryRequest req) {
        return trainingService.getTrainerTrainings(
                req.getUsername(),
                req.getPeriodFrom(),
                req.getPeriodTo(),
                req.getTraineeName()
        ).stream().map(t -> new TrainingResponse(
                t.getTrainingName(),
                t.getTrainingDate(),
                t.getTrainingType().getTrainingTypeName(),
                t.getTrainingDuration(),
                t.getTrainee().getUser().getUsername()
        )).toList();
    }

    public void createTraining(AddTrainingRequest req) {
        Training t = Training.builder()
                .trainingName(req.getTrainingName())
                .trainingDate(req.getTrainingDate())
                .trainingDuration(req.getTrainingDuration())
                .build();
        trainingService.createTraining(req.getTraineeUsername(), req.getTrainerUsername(), t);
    }

    public List<TrainingTypeResponse> getAllTrainingTypes() {
        return trainingService.findAll().stream()
                .map(t -> new TrainingTypeResponse(
                        t.getTrainingType().getId(),
                        t.getTrainingType().getTrainingTypeName()))
                .toList();
    }

    public boolean authenticateTrainee(String username, String password) {
        return traineeService.authenticate(username, password);
    }

    public boolean authenticateTrainer(String username, String password) {
        return trainerService.authenticate(username, password);
    }

    private TraineeProfileResponse toTraineeProfile(Trainee t) {
        return new TraineeProfileResponse(
                t.getUser().getUsername(),
                t.getUser().getFirstName(),
                t.getUser().getLastName(),
                t.getDateOfBirth(),
                t.getAddress(),
                t.getUser().getIsActive(),
                t.getTrainers().stream().map(this::toTrainerSummary).toList()
        );
    }

    private TrainerProfileResponse toTrainerProfile(Trainer tr) {
        return new TrainerProfileResponse(
                tr.getUser().getUsername(),
                tr.getUser().getFirstName(),
                tr.getUser().getLastName(),
                tr.getSpecialization().getTrainingTypeName(),
                tr.getUser().getIsActive(),
                tr.getTrainees().stream().map(this::toTraineeSummary).toList()
        );
    }

    private TrainerSummaryDTO toTrainerSummary(Trainer tr) {
        return new TrainerSummaryDTO(
                tr.getUser().getUsername(),
                tr.getUser().getFirstName(),
                tr.getUser().getLastName(),
                tr.getSpecialization().getTrainingTypeName()
        );
    }

    private TraineeSummaryDTO toTraineeSummary(Trainee t) {
        return new TraineeSummaryDTO(
                t.getUser().getUsername(),
                t.getUser().getFirstName(),
                t.getUser().getLastName()
        );
    }
}