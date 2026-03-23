package com.example.gymcrm.controller;

import com.example.gymcrm.dto.AuthCredentials;
import com.example.gymcrm.dto.request.*;
import com.example.gymcrm.dto.response.TraineeProfileResponse;
import com.example.gymcrm.dto.response.TrainerSummaryDTO;
import com.example.gymcrm.dto.response.TrainingResponse;
import com.example.gymcrm.facade.GymFacade;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/trainees")
public class TraineeController {

    private final GymFacade facade;

    public TraineeController(GymFacade facade) {
        this.facade = facade;
    }

    @PostMapping
    public AuthCredentials register(@Valid @RequestBody TraineeRegistrationRequest req) {
        return facade.createTrainee(req);
    }

    @GetMapping
    public TraineeProfileResponse get(@RequestParam String username) {
        return facade.getTraineeByUsername(username);
    }

    @PutMapping
    public TraineeProfileResponse update(@Valid @RequestBody UpdateTraineeRequest req) {
        return facade.updateTrainee(req);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam String username) {
        facade.deleteTrainee(username);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/activate")
    public ResponseEntity<Void> activate(@Valid @RequestBody ActivateRequest req) {
        facade.setTraineeActiveStatus(req);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/trainers/unassigned")
    public List<TrainerSummaryDTO> unassigned(@RequestParam String username) {
        return facade.getUnassignedTrainers(username);
    }

    @PutMapping("/trainers")
    public List<TrainerSummaryDTO> updateTrainers(@Valid @RequestBody UpdateTraineeTrainersRequest req) {
        return facade.updateTraineeTrainers(req);
    }

    @GetMapping("/trainings")
    public List<TrainingResponse> getTrainings(
            @RequestParam String username,
            @RequestParam(required = false) LocalDate periodFrom,
            @RequestParam(required = false) LocalDate periodTo,
            @RequestParam(required = false) String trainerName,
            @RequestParam(required = false) Long trainingTypeId) {
        TraineeTrainingQueryRequest req = new TraineeTrainingQueryRequest();
        req.setUsername(username);
        req.setPeriodFrom(periodFrom);
        req.setPeriodTo(periodTo);
        req.setTrainerName(trainerName);
        req.setTrainingTypeId(trainingTypeId);
        return facade.getTraineeTrainings(req);
    }
}