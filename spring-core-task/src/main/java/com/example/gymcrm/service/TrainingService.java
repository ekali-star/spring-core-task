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

    private static final Logger log =
            LoggerFactory.getLogger(TrainingService.class);

    private TrainingDao trainingDao;

    private final AtomicLong idGenerator = new AtomicLong(1);

    @Autowired
    public void setTrainingDao(TrainingDao trainingDao) {
        this.trainingDao = trainingDao;
    }

    public Training create(Training training) {

        Long id = idGenerator.getAndIncrement();

        trainingDao.save(id, training);

        log.info("Training created: id={}", id);

        return training;
    }

    public Training findById(Long id) {
        return trainingDao.findById(id);
    }

    public Collection<Training> findAll() {
        return trainingDao.findAll();
    }

    public Collection<Training> findByTraineeId(long l) {
        return trainingDao.findAll().stream()
                .filter(t -> t.getTraineeId() == l)
                .toList();
    }

    public Collection<Training> findByTrainerId(long l) {
        return trainingDao.findAll().stream()
                .filter(t -> t.getTrainerId() == l)
                .toList();
    }

    public Collection<Training> findByDate(LocalDate today) {
        return trainingDao.findAll().stream()
                .filter(t -> t.getDate().equals(today))
                .toList();
    }
}
