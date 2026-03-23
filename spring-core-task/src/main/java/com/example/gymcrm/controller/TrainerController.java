package com.example.gymcrm.controller;

import com.example.gymcrm.dto.AuthCredentials;
import com.example.gymcrm.dto.request.*;
import com.example.gymcrm.dto.response.TrainerProfileResponse;
import com.example.gymcrm.dto.response.TrainingResponse;
import com.example.gymcrm.facade.GymFacade;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/trainers")
public class TrainerController {

    private final GymFacade facade;

    public TrainerController(GymFacade facade) {
        this.facade = facade;
    }

    @PostMapping
    public AuthCredentials register(@Valid @RequestBody TrainerRegistrationRequest req) {
        return facade.createTrainer(req);
    }

    @GetMapping
    public TrainerProfileResponse get(@RequestParam String username) {
        return facade.getTrainerByUsername(username);
    }

    @PutMapping
    public TrainerProfileResponse update(@Valid @RequestBody UpdateTrainerRequest req) {
        return facade.updateTrainer(req);
    }

    @PatchMapping("/activate")
    public ResponseEntity<Void> activate(@Valid @RequestBody ActivateRequest req) {
        facade.setTrainerActiveStatus(req);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/trainings")
    public List<TrainingResponse> getTrainings(
            @RequestParam String username,
            @RequestParam(required = false) LocalDate periodFrom,
            @RequestParam(required = false) LocalDate periodTo,
            @RequestParam(required = false) String traineeName) {
        TrainerTrainingQueryRequest req = new TrainerTrainingQueryRequest();
        req.setUsername(username);
        req.setPeriodFrom(periodFrom);
        req.setPeriodTo(periodTo);
        req.setTraineeName(traineeName);
        return facade.getTrainerTrainings(req);
    }
}