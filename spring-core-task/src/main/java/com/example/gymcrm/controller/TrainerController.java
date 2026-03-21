package com.example.gymcrm.controller;


import com.example.gymcrm.dto.request.*;
import com.example.gymcrm.dto.response.*;
import com.example.gymcrm.facade.GymFacade;
import com.example.gymcrm.model.*;

import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/trainers")
public class TrainerController {

    private final GymFacade facade;

    public TrainerController(GymFacade facade) {
        this.facade = facade;
    }

    @PostMapping
    public Object register(@RequestBody TrainerRegistrationRequest req) {

        User user = User.create(req.getFirstName(), req.getLastName());

        TrainingType type = TrainingType.create(req.getSpecializationId());

        Trainer t = new Trainer();
        t.setUser(user);
        t.setSpecialization(type);

        return facade.createTrainer(t);
    }

    @PatchMapping("/activate")
    public void activate(@RequestBody ActivateRequest req) {
        if (req.getIsActive()) {
            facade.activateTrainer(req.getUsername(), req.getPassword());
        } else {
            facade.deactivateTrainer(req.getUsername(), req.getPassword());
        }
    }
}