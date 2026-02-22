package com.example.gymcrm.service;

import com.example.gymcrm.dao.TrainingDao;
import com.example.gymcrm.model.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TrainingService {

    private static final Logger log = LoggerFactory.getLogger(TrainingService.class);

    private TrainingDao trainingDao;
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Autowired
    public void setTrainingDao(TrainingDao trainingDao) {
        this.trainingDao = trainingDao;
    }

    public Training create(Training training) {
        log.debug("Creating new training: {}", training.getTrainingName());

        Long id = idGenerator.getAndIncrement();
        training.setId(id);

        trainingDao.save(id, training);

        log.info("Training created successfully - ID: {}, Name: {}, Type: {}, Date: {}",
                id, training.getTrainingName(), training.getTrainingType(), training.getTrainingDate());

        return training;
    }

    public Training findById(Long id) {
        Training training = trainingDao.findById(id);
        if (training == null) {
            log.debug("Training not found with ID: {}", id);
        } else {
            log.debug("Training found - ID: {}, Name: {}", id, training.getTrainingName());
        }
        return training;
    }

    public Collection<Training> findAll() {
        Collection<Training> trainings = trainingDao.findAll();
        log.debug("Retrieved {} trainings from storage", trainings.size());
        return trainings;
    }

    public Collection<Training> findByTraineeId(Long traineeId) {
        Collection<Training> trainings = trainingDao.findAll().stream()
                .filter(t -> t.getTraineeId() != null && t.getTraineeId().equals(traineeId))
                .toList();
        log.debug("Found {} trainings for trainee ID: {}", trainings.size(), traineeId);
        return trainings;
    }

    public Collection<Training> findByTrainerId(Long trainerId) {
        Collection<Training> trainings = trainingDao.findAll().stream()
                .filter(t -> t.getTrainerId() != null && t.getTrainerId().equals(trainerId))
                .toList();
        log.debug("Found {} trainings for trainer ID: {}", trainings.size(), trainerId);
        return trainings;
    }

    public Collection<Training> findByDate(LocalDate date) {
        Collection<Training> trainings = trainingDao.findAll().stream()
                .filter(t -> t.getTrainingDate() != null && t.getTrainingDate().equals(date))
                .toList();
        log.debug("Found {} trainings for date: {}", trainings.size(), date);
        return trainings;
    }
}