package com.example.gymcrm.controller;

import com.example.gymcrm.dto.AuthCredentials;
import com.example.gymcrm.dto.request.*;
import com.example.gymcrm.dto.response.TraineeProfileResponse;
import com.example.gymcrm.dto.response.TrainerSummaryDTO;
import com.example.gymcrm.dto.response.TrainingResponse;
import com.example.gymcrm.facade.GymFacade;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trainees")
public class TraineeController {

    private final GymFacade facade;

    public TraineeController(GymFacade facade) {
        this.facade = facade;
    }

    @PostMapping
    public Object register(@Valid @RequestBody TraineeRegistrationRequest req) {
        return facade.createTrainee(req);
    }

    @GetMapping
    public TraineeProfileResponse get(
            @RequestParam String username,
            @RequestHeader String authUsername,
            @RequestHeader String authPassword) {

        return facade.getTraineeByUsername(
                new AuthCredentials(authUsername, authPassword),
                username
        );
    }

    @PutMapping
    public TraineeProfileResponse update(
            @Valid @RequestBody UpdateTraineeRequest req,
            @RequestHeader String authUsername,
            @RequestHeader String authPassword) {

        return facade.updateTrainee(
                new AuthCredentials(authUsername, authPassword),
                req.getUsername(),
                req
        );
    }

    @DeleteMapping
    public void delete(
            @RequestParam String username,
            @RequestHeader String authUsername,
            @RequestHeader String authPassword) {

        facade.deleteTrainee(
                new AuthCredentials(authUsername, authPassword),
                username
        );
    }

    @PatchMapping("/activate")
    public void activate(
            @Valid @RequestBody ActivateRequest req,
            @RequestHeader String authUsername,
            @RequestHeader String authPassword) {

        facade.setTraineeActiveStatus(
                new AuthCredentials(authUsername, authPassword),
                req.getUsername(),
                req.getIsActive()
        );
    }

    @GetMapping("/unassigned-trainers")
    public List<TrainerSummaryDTO> unassigned(
            @RequestParam String username,
            @RequestHeader String authUsername,
            @RequestHeader String authPassword) {

        return facade.getUnassignedTrainers(
                new AuthCredentials(authUsername, authPassword),
                username
        );
    }

    @PutMapping("/trainers")
    public List<TrainerSummaryDTO> updateTrainers(
            @Valid @RequestBody UpdateTraineeTrainersRequest req,
            @RequestHeader String authUsername,
            @RequestHeader String authPassword) {

        return facade.updateTraineeTrainers(
                new AuthCredentials(authUsername, authPassword),
                req.getUsername(),
                req.getTrainerUsernames()
        );
    }

    @GetMapping("/trainings")
    public List<TrainingResponse> getTrainings(
            @RequestParam String username,
            @ModelAttribute TraineeTrainingQueryRequest query,
            @RequestHeader String authUsername,
            @RequestHeader String authPassword) {

        return facade.getTraineeTrainings(
                new AuthCredentials(authUsername, authPassword),
                username,
                query.getPeriodFrom(),
                query.getPeriodTo(),
                query.getTrainerName(),
                query.getTrainingTypeId()
        );
    }
}