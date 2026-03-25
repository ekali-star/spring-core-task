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
        if (!traineeService.authenticate(req.getUsername(), req.getPassword()) &&
                !trainerService.authenticate(req.getUsername(), req.getPassword())) {
            throw new IllegalArgumentException("Authentication failed");
        }
    }

    public void changePassword(ChangePasswordRequest req) {
        Auth auth = new Auth(req.getUsername(), req.getOldPassword());

        if (traineeService.authenticate(auth)) {
            traineeService.changePassword(auth, req.getNewPassword());
        } else if (trainerService.authenticate(auth)) {
            trainerService.changePassword(auth, req.getNewPassword());
        } else {
            throw new IllegalArgumentException("Authentication failed");
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

    public TraineeProfileResponse getTraineeByUsername(AuthCredentials auth, String username) {
        authenticate(auth);
        return toTraineeProfile(traineeService.findByUsername(username));
    }

    public TraineeProfileResponse updateTrainee(AuthCredentials auth, String username, UpdateTraineeRequest req) {
        Auth a = new Auth(auth.getUsername(), auth.getPassword());

        Trainee patch = new Trainee();
        patch.setUser(User.builder()
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .build());
        patch.setDateOfBirth(req.getDateOfBirth());
        patch.setAddress(req.getAddress());

        return toTraineeProfile(traineeService.updateTrainee(a, username, patch));
    }

    public void deleteTrainee(AuthCredentials auth, String username) {
        authenticate(auth);
        traineeService.deleteTrainee(username);
    }

    public void setTraineeActiveStatus(AuthCredentials auth, String username, Boolean isActive) {
        authenticate(auth);
        traineeService.setActiveStatus(username, isActive);
    }

    public List<TrainerSummaryDTO> getUnassignedTrainers(AuthCredentials auth, String username) {
        authenticate(auth);
        return traineeService.getUnassignedTrainers(username)
                .stream()
                .map(this::toTrainerSummary)
                .toList();
    }

    public List<TrainerSummaryDTO> updateTraineeTrainers(AuthCredentials auth, String username, List<String> trainers) {
        authenticate(auth);
        traineeService.updateTrainers(username, trainers);
        return getTraineeByUsername(auth, username).getTrainers();
    }

    public List<TrainingResponse> getTraineeTrainings(
            AuthCredentials auth,
            String username,
            java.time.LocalDate from,
            java.time.LocalDate to,
            String trainerName,
            Long typeId) {

        authenticate(auth);

        return trainingService.getTraineeTrainings(username, from, to, trainerName, typeId)
                .stream()
                .map(this::toTrainingResponseForTrainee)
                .toList();
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

    public TrainerProfileResponse getTrainerByUsername(AuthCredentials auth, String username) {
        authenticate(auth);
        return toTrainerProfile(trainerService.findByUsername(username));
    }

    public TrainerProfileResponse updateTrainer(AuthCredentials auth, String username, UpdateTrainerRequest req) {
        Auth a = new Auth(auth.getUsername(), auth.getPassword());

        Trainer patch = new Trainer();
        patch.setUser(User.builder()
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .build());

        return toTrainerProfile(trainerService.updateTrainer(a, username, patch));
    }

    public void setTrainerActiveStatus(AuthCredentials auth, String username, Boolean isActive) {
        authenticate(auth);
        trainerService.setActiveStatus(username, isActive);
    }

    public List<TrainingResponse> getTrainerTrainings(
            AuthCredentials auth,
            String username,
            java.time.LocalDate from,
            java.time.LocalDate to,
            String traineeName) {

        authenticate(auth);

        return trainingService.getTrainerTrainings(username, from, to, traineeName)
                .stream()
                .map(this::toTrainingResponseForTrainer)
                .toList();
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
        return trainingService.findAll()
                .stream()
                .map(t -> new TrainingTypeResponse(
                        t.getTrainingType().getId(),
                        t.getTrainingType().getTrainingTypeName()))
                .toList();
    }

    private void authenticate(AuthCredentials auth) {
        if (!traineeService.authenticate(auth.getUsername(), auth.getPassword()) &&
                !trainerService.authenticate(auth.getUsername(), auth.getPassword())) {
            throw new IllegalArgumentException("Authentication failed");
        }
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

    private TrainingResponse toTrainingResponseForTrainee(Training t) {
        return new TrainingResponse(
                t.getTrainingName(),
                t.getTrainingDate(),
                t.getTrainingType().getTrainingTypeName(),
                t.getTrainingDuration(),
                t.getTrainer().getUser().getUsername()
        );
    }

    private TrainingResponse toTrainingResponseForTrainer(Training t) {
        return new TrainingResponse(
                t.getTrainingName(),
                t.getTrainingDate(),
                t.getTrainingType().getTrainingTypeName(),
                t.getTrainingDuration(),
                t.getTrainee().getUser().getUsername()
        );
    }
}