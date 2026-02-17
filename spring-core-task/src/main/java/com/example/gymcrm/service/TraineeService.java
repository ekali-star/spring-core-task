package com.example.gymcrm.service;

import com.example.gymcrm.dao.TraineeDao;
import com.example.gymcrm.model.Trainee;
import com.example.gymcrm.service.CredentialsGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TraineeService {

    private static final Logger log =
            LoggerFactory.getLogger(TraineeService.class);

    private TraineeDao traineeDao;

    private final AtomicLong idGenerator = new AtomicLong(1);

    @Autowired
    public void setTraineeDao(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    public Trainee create(Trainee trainee) {

        Long id = idGenerator.getAndIncrement();

        String username =
                CredentialsGenerator.generateUsername(
                        trainee.getFirstName(),
                        trainee.getLastName(),
                        traineeDao.findAll()
                );

        String password = CredentialsGenerator.generatePassword();

        trainee.setUsername(username);
        trainee.setPassword(password);
        trainee.setActive(true);
        trainee.setUserID(id);

        traineeDao.save(id, trainee);

        log.info("Trainee created: id={}, username={}", id, username);

        return trainee;
    }

    public void update(Long id, Trainee trainee) {

        Trainee existing = traineeDao.findById(id);
        if (existing == null) {
            throw new IllegalArgumentException("Trainee not found: " + id);
        }

        trainee.setUserID(id);
        traineeDao.save(id, trainee);

        log.info("Trainee updated: id={}", id);
    }

    public void delete(Long id) {
        traineeDao.delete(id);
        log.info("Trainee deleted: id={}", id);
    }

    public Trainee findById(Long id) {
        return traineeDao.findById(id);
    }

    public Collection<Trainee> findAll() {
        return traineeDao.findAll();
    }
}
