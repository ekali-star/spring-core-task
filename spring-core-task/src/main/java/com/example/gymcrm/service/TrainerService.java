package com.example.gymcrm.service;

import com.example.gymcrm.dao.TrainerDao;
import com.example.gymcrm.model.Trainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TrainerService {

    private static final Logger log = LoggerFactory.getLogger(TrainerService.class);

    private TrainerDao trainerDao;
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Autowired
    public void setTrainerDao(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    public Trainer create(Trainer trainer) {
        log.debug("Creating new trainer: {} {}", trainer.getFirstName(), trainer.getLastName());

        Long id = idGenerator.getAndIncrement();

        String username = CredentialsGenerator.generateUsername(
                trainer.getFirstName(),
                trainer.getLastName(),
                trainerDao.findAll()
        );
        String password = CredentialsGenerator.generatePassword();

        trainer.setUsername(username);
        trainer.setPassword(password);
        trainer.setActive(true);
        trainer.setId(id);

        trainerDao.save(id, trainer);

        log.info("Trainer created successfully - ID: {}, Username: {}, Specialization: {}",
                id, username, trainer.getSpecialization());

        return trainer;
    }

    public Trainer update(Long id, Trainer trainer) {
        log.debug("Updating trainer with ID: {}", id);

        Trainer existing = trainerDao.findById(id);
        if (existing == null) {
            log.error("Update failed - Trainer not found with ID: {}", id);
            throw new IllegalArgumentException("Trainer not found with id: " + id);
        }

        // Preserve credentials and ID
        trainer.setId(id);
        trainer.setUsername(existing.getUsername());
        trainer.setPassword(existing.getPassword());

        trainerDao.save(id, trainer);

        log.info("Trainer updated successfully - ID: {}, Username: {}", id, trainer.getUsername());
        return trainer;
    }

    public Trainer findById(Long id) {
        Trainer trainer = trainerDao.findById(id);
        if (trainer == null) {
            log.debug("Trainer not found with ID: {}", id);
        } else {
            log.debug("Trainer found - ID: {}, Username: {}", id, trainer.getUsername());
        }
        return trainer;
    }

    public Collection<Trainer> findAll() {
        Collection<Trainer> trainers = trainerDao.findAll();
        log.debug("Retrieved {} trainers from storage", trainers.size());
        return trainers;
    }
}