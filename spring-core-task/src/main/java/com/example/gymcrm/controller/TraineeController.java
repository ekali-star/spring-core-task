package com.example.gymcrm.controller;

import com.example.gymcrm.dto.AuthCredentials;
import com.example.gymcrm.dto.request.ActivateRequest;
import com.example.gymcrm.dto.request.TraineeRegistrationRequest;
import com.example.gymcrm.dto.request.UpdateTraineeRequest;
import com.example.gymcrm.dto.request.UpdateTraineeTrainersRequest;
import com.example.gymcrm.dto.response.TraineeProfileResponse;
import com.example.gymcrm.dto.response.TrainerSummaryDTO;
import com.example.gymcrm.facade.GymFacade;
import com.example.gymcrm.model.Trainee;
import com.example.gymcrm.model.User;
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
    public AuthCredentials register(@RequestBody TraineeRegistrationRequest req) {
        User user = User.builder().firstName(req.getFirstName()).lastName(req.getLastName()).build();

        Trainee t = new Trainee();
        t.setUser(user);
        t.setDateOfBirth(req.getDateOfBirth());
        t.setAddress(req.getAddress());

        return facade.createTrainee(t);
    }

    @GetMapping
    public TraineeProfileResponse get(@RequestParam String username) {
        Trainee t = facade.getTraineeByUsername(username);

        return new TraineeProfileResponse(
                t.getUser().getUsername(),
                t.getUser().getFirstName(),
                t.getUser().getLastName(),
                t.getDateOfBirth(),
                t.getAddress(),
                t.getUser().getIsActive(),
                t.getTrainers().stream()
                        .map(tr -> new TrainerSummaryDTO(
                                tr.getUser().getUsername(),
                                tr.getUser().getFirstName(),
                                tr.getUser().getLastName(),
                                tr.getSpecialization().getTrainingTypeName()
                        )).toList()
        );
    }

    @PutMapping
    public TraineeProfileResponse update(@RequestBody UpdateTraineeRequest req) {

        User user = User.builder().firstName(req.getFirstName()).lastName(req.getLastName()).build();

        Trainee t = new Trainee();
        t.setUser(user);
        t.setDateOfBirth(req.getDateOfBirth());
        t.setAddress(req.getAddress());

        Trainee updated = facade.updateTrainee(req.getUsername(), req.getPassword(), t);

        return get(updated.getUser().getUsername());
    }

    @DeleteMapping
    public void delete(@RequestParam String username,
                       @RequestParam String password) {
        facade.deleteTrainee(username, password);
    }

    @PatchMapping("/activate")
    public void activate(@RequestBody ActivateRequest req) {
        if (req.getIsActive()) {
            facade.activateTrainee(req.getUsername(), req.getPassword());
        } else {
            facade.deactivateTrainee(req.getUsername(), req.getPassword());
        }
    }

    @GetMapping("/trainers/unassigned")
    public List<TrainerSummaryDTO> unassigned(@RequestParam String username,
                                              @RequestParam String password) {

        return facade.getUnassignedTrainers(username, password)
                .stream()
                .map(tr -> new TrainerSummaryDTO(
                        tr.getUser().getUsername(),
                        tr.getUser().getFirstName(),
                        tr.getUser().getLastName(),
                        tr.getSpecialization().getTrainingTypeName()
                )).toList();
    }

    @PutMapping("/trainers")
    public List<TrainerSummaryDTO> updateTrainers(@RequestBody UpdateTraineeTrainersRequest req) {

        facade.updateTraineeTrainers(
                req.getUsername(),
                req.getPassword(),
                req.getTrainerUsernames()
        );

        return get(req.getUsername()).getTrainers();
    }
}