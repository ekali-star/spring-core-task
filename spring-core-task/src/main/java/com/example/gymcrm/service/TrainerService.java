package com.example.gymcrm.service;

import com.example.gymcrm.dao.TrainerDao;
import com.example.gymcrm.model.Trainer;
import com.example.gymcrm.service.CredentialsGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TrainerService {

    private static final Logger log =
            LoggerFactory.getLogger(TrainerService.class);

    private TrainerDao trainerDao;

    private final AtomicLong idGenerator = new AtomicLong(1);

    @Autowired
    public void setTrainerDao(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    public Trainer create(Trainer trainer) {

        Long id = idGenerator.getAndIncrement();

        String username =
                CredentialsGenerator.generateUsername(
                        trainer.getFirstName(),
                        trainer.getLastName(),
                        trainerDao.findAll()
                );

        String password = CredentialsGenerator.generatePassword();

        trainer.setUsername(username);
        trainer.setPassword(password);
        trainer.setActive(true);
        trainer.setUserID(id);

        trainerDao.save(id, trainer);

        log.info("Trainer created: id={}, username={}", id, username);

        return trainer;
    }

    public void update(Long id, Trainer trainer) {

        Trainer existing = trainerDao.findById(id);
        if (existing == null) {
            throw new IllegalArgumentException("Trainer not found: " + id);
        }

        trainer.setUserID(id);
        trainerDao.save(id, trainer);

        log.info("Trainer updated: id={}", id);
    }

    public Trainer findById(Long id) {
        return trainerDao.findById(id);
    }

    public Collection<Trainer> findAll() {
        return trainerDao.findAll();
    }
}
