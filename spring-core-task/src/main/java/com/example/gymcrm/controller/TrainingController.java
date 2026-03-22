package com.example.gymcrm.controller;


import com.example.gymcrm.dto.request.AddTrainingRequest;
import com.example.gymcrm.facade.GymFacade;
import com.example.gymcrm.model.Training;
import com.example.gymcrm.model.TrainingType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/trainings")
public class TrainingController {

    private final GymFacade facade;

    public TrainingController(GymFacade facade) {
        this.facade = facade;
    }

    @PostMapping
    public void add(@RequestBody AddTrainingRequest req) {

        Training t = Training.builder()
                .trainingName(req.getTrainingName())
                .trainingDate(req.getTrainingDate())
                .trainingDuration(req.getTrainingDuration())
                .build();

        TrainingType type = TrainingType.builder().id(req.getTrainingTypeId()).build();
        t.setTrainingType(type);

        facade.createTraining(
                req.getTraineeUsername(),
                req.getTrainerUsername(),
                t
        );
    }
}