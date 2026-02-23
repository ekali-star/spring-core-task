package com.example.gymcrm.service;

import com.example.gymcrm.dao.TraineeDao;
import com.example.gymcrm.model.Trainee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TraineeService {

    private static final Logger log = LoggerFactory.getLogger(TraineeService.class);

    private TraineeDao traineeDao;
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Autowired
    public void setTraineeDao(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    public Trainee create(Trainee trainee) {
        log.debug("Creating new trainee: {} {}", trainee.getFirstName(), trainee.getLastName());

        Long id = idGenerator.getAndIncrement();

        String username = CredentialsGenerator.generateUsername(
                trainee.getFirstName(),
                trainee.getLastName(),
                traineeDao.findAll()
        );
        String password = CredentialsGenerator.generatePassword();

        trainee.setUsername(username);
        trainee.setPassword(password);
        trainee.setActive(true);
        trainee.setId(id);

        traineeDao.save(id, trainee);

        log.info("Trainee created successfully - ID: {}, Username: {}, Full Name: {} {}",
                id, username, trainee.getFirstName(), trainee.getLastName());

        return trainee;
    }

    public Trainee update(Long id, Trainee trainee) {
        log.debug("Updating trainee with ID: {}", id);

        Trainee existing = traineeDao.findById(id);
        if (existing == null) {
            log.error("Update failed - Trainee not found with ID: {}", id);
            throw new IllegalArgumentException("Trainee not found with id: " + id);
        }

        trainee.setId(id);
        trainee.setUsername(existing.getUsername());
        trainee.setPassword(existing.getPassword());

        traineeDao.save(id, trainee);

        log.info("Trainee updated successfully - ID: {}, Username: {}", id, trainee.getUsername());
        return trainee;
    }

    public void delete(Long id) {
        log.debug("Deleting trainee with ID: {}", id);

        Trainee trainee = traineeDao.findById(id);
        if (trainee == null) {
            log.warn("Delete attempted for non-existent trainee - ID: {}", id);
            return;
        }

        traineeDao.delete(id);
        log.info("Trainee deleted successfully - ID: {}, Username: {}", id, trainee.getUsername());
    }

    public Trainee findById(Long id) {
        Trainee trainee = traineeDao.findById(id);
        if (trainee == null) {
            log.debug("Trainee not found with ID: {}", id);
        } else {
            log.debug("Trainee found - ID: {}, Username: {}", id, trainee.getUsername());
        }
        return trainee;
    }

    public Collection<Trainee> findAll() {
        Collection<Trainee> trainees = traineeDao.findAll();
        log.debug("Retrieved {} trainees from storage", trainees.size());
        return trainees;
    }
}