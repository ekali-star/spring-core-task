package com.example.gymcrm.controller;

import com.example.gymcrm.dto.response.TrainingTypeResponse;
import com.example.gymcrm.facade.GymFacade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/training-types")
public class TrainingTypeController {

    private final GymFacade facade;

    public TrainingTypeController(GymFacade facade) {
        this.facade = facade;
    }

    @GetMapping
    public List<TrainingTypeResponse> getAll() {
        return facade.getAllTrainings().stream()
                .map(t -> new TrainingTypeResponse(
                        t.getTrainingType().getId(),
                        t.getTrainingType().getTrainingTypeName()
                )).toList();
    }
}