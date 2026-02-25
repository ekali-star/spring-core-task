package com.example.gymcrm.service;

import com.example.gymcrm.dao.TraineeDao;
import com.example.gymcrm.model.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class TraineeService extends UserService<Trainee> {
    private TraineeDao traineeDao;
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Autowired
    public void setTraineeDao(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    @Override
    protected TraineeDao getDao() {
        return traineeDao;
    }

    @Override
    protected Trainee saveWithId(Trainee user) {
        long id = idGenerator.getAndIncrement();
        user.setId(id);
        traineeDao.save(id, user);
        return user;
    }

    @Override
    protected Long getId(Trainee user) {
        return user.getId();
    }
}