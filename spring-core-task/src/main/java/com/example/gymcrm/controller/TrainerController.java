package com.example.gymcrm.controller;


import com.example.gymcrm.dto.Auth;
import com.example.gymcrm.dto.AuthCredentials;
import com.example.gymcrm.dto.request.ActivateRequest;
import com.example.gymcrm.dto.request.TrainerRegistrationRequest;
import com.example.gymcrm.facade.GymFacade;
import com.example.gymcrm.model.Trainer;
import com.example.gymcrm.model.TrainingType;
import com.example.gymcrm.model.User;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/trainers")
public class TrainerController {

    private final GymFacade facade;

    public TrainerController(GymFacade facade) {
        this.facade = facade;
    }

    @PostMapping
    public AuthCredentials register(@RequestBody TrainerRegistrationRequest req) {

        User user = User.builder().firstName(req.getFirstName()).lastName(req.getLastName()).build();

        TrainingType type = TrainingType.builder().id(req.getSpecializationId()).build();

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