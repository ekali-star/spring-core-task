package com.example.gymcrm.controller;


import com.example.gymcrm.dto.request.*;
import com.example.gymcrm.dto.response.*;
import com.example.gymcrm.facade.GymFacade;
import com.example.gymcrm.model.*;

import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/trainings")
public class TrainingController {

    private final GymFacade facade;

    public TrainingController(GymFacade facade) {
        this.facade = facade;
    }

    @PostMapping
    public void add(@RequestBody AddTrainingRequest req) {

        Training t = Training.create(req.getTrainingName(), req.getTrainingDate(), req.getTrainingDuration());

        TrainingType type = TrainingType.create(req.getTrainingTypeId());
        t.setTrainingType(type);

        facade.createTraining(
                req.getTraineeUsername(),
                req.getTrainerUsername(),
                t
        );
    }
}