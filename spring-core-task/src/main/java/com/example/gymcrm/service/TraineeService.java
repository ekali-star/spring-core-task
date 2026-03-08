package com.example.gymcrm.service;

import com.example.gymcrm.model.Trainee;
import com.example.gymcrm.model.Trainer;
import com.example.gymcrm.repository.TraineeRepository;
import com.example.gymcrm.repository.TrainerRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TraineeService extends UserService<Trainee> {

    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;

    public TraineeService(TraineeRepository traineeRepository, TrainerRepository trainerRepository) {
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
    }

    @Override
    protected JpaRepository<Trainee, Long> getRepository() {
        return traineeRepository;
    }

    @Override
    protected Collection<Trainee> findAllUsers() {
        return traineeRepository.findAll();
    }

    @Override
    protected Long getId(Trainee user) {
        return user.getId();
    }

    @Override
    protected Optional<Trainee> findByUsernameOptional(String username) {
        return traineeRepository.findByUser_Username(username);
    }

    public List<Trainer> getUnassignedTrainers(String traineeUsername) {
        return trainerRepository.findNotAssignedToTrainee(traineeUsername);
    }

    public void updateTrainers(String traineeUsername, List<Long> trainerIds) {
        Trainee trainee = traineeRepository.findByUser_Username(traineeUsername)
                .orElseThrow(() -> new IllegalArgumentException("Trainee not found"));

        List<Trainer> trainers = trainerRepository.findAllById(trainerIds);
        trainee.setTrainers(trainers);
        traineeRepository.save(trainee);

        log.info("Trainee {} trainers updated", traineeUsername);
    }

}